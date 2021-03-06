package tr.com.metu.ramazan.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import tr.com.metu.ramazan.excel.Constants;
import tr.com.metu.ramazan.excel.ExcelReader;
import tr.com.metu.ramazan.model.BasketType;
import tr.com.metu.ramazan.model.Demand;
import tr.com.metu.ramazan.model.TimeSlot;
import tr.com.metu.ramazan.utils.DemandService;

@Component
public class DemandRepository {

    private final ExcelReader excelReader;
    private final ProductRepository productDao;
    private final StationRepository stationDao;
    private final TimeSlotRepository timeSlotRepository;
    private List<Demand> demands;

    public DemandRepository(ExcelReader excelReader,
            ProductRepository productDao,
            StationRepository stationDao,
            TimeSlotRepository timeSlotRepository) {
        this.productDao = productDao;
        this.excelReader = excelReader;
        this.stationDao = stationDao;
        this.timeSlotRepository = timeSlotRepository;
    }

    @PostConstruct
    private void init() {
        List<Demand> constructDemands = constructDemands();
        List<Demand> mergeAll = DemandService.mergeAll(constructDemands);
        demands = splitDemands(mergeAll);

    }

    private static List<Demand> splitDemands(List<Demand> demands) {
        return demands.stream()
                .flatMap(demand -> demand.splitDemand().stream())
                .collect(Collectors.toList());
    }

    public List<Demand> getDemands() {
        return demands;
    }

    public List<Demand> findDemandsByTimeSlotAndBasketType(TimeSlot timeSlot,
            BasketType basketType) {
        return demands.stream()
                .filter(demand -> demand.getTimeSlot().equals(timeSlot))
                .filter(demand -> demand.getBasketType().equals(basketType))
                .collect(Collectors.toList());
    }

    private List<Demand> constructDemands() {
        return excelReader.getRowStreamFromSheet(1).flatMap(row ->
        // skip first timeSlot
        IntStream.range(0, Constants.TIME_SLOT_COUNT)
                .boxed()
                .flatMap(timeSlot -> Stream.iterate(timeSlot * Constants.STATION_COUNT + 1, j -> j + 1)
                        .limit(Constants.STATION_COUNT)
                        .filter(j -> row.getCell(j).getNumericCellValue() > 0)
                        .map(j -> new Demand(
                                productDao
                                        .findProductById(
                                                (int) row.getCell(0).getNumericCellValue()),
                                stationDao.findStationById(
                                        j - (timeSlot * Constants.STATION_COUNT + 1)),
                                row.getCell(j).getNumericCellValue(),
                                timeSlotRepository.findTimeSlotByRank(timeSlot)))))
                .collect(Collectors.toList());
    }
}
