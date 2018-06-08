package tr.com.metu.ramazan.commands;

import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import tr.com.metu.ramazan.model.Demand;
import tr.com.metu.ramazan.model.Solution;
import tr.com.metu.ramazan.model.Tour;
import tr.com.metu.ramazan.repository.BasketTypeRepository;
import tr.com.metu.ramazan.repository.DemandRepository;
import tr.com.metu.ramazan.repository.TimeSlotRepository;
import tr.com.metu.ramazan.utils.CalculationService;
import tr.com.metu.ramazan.utils.TourService;

@ShellComponent
public class Commands {

    private final DemandRepository demandRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final BasketTypeRepository basketTypeRepository;

    public Commands(DemandRepository demandRepository,
            TimeSlotRepository timeSlotRepository,
            BasketTypeRepository basketTypeRepository) {
        this.demandRepository = demandRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.basketTypeRepository = basketTypeRepository;
    }

    @ShellMethod("Runs the algorithm")
    public void run() {
        List<Solution> solutions = CalculationService
                .run(new Solution(timeSlotRepository.getTimeSlots()));
        System.out.println("##### PRINTING FOUND SOLUTIONS");
        IntStream.range(0, solutions.size()).forEach(i -> {
            if (i > 0 && solutions.get(i).getTotalTourCount() == solutions.get(i - 1).getTotalTourCount())
                return;
            System.out.println(solutions.get(i));
        });
    }

    @PostConstruct
    private void init() {
        // add demands to timeSlots
        timeSlotRepository.getTimeSlots().stream()
                .forEach(timeSlot -> basketTypeRepository.getBasketTypes().stream()
                        .forEach(basketType -> {
                            List<Demand> findDemandsByTimeSlotAndBasketType = demandRepository
                                    .findDemandsByTimeSlotAndBasketType(timeSlot, basketType);

                            List<Tour> tourFromDemands = TourService
                                    .getToursFromDemands(findDemandsByTimeSlotAndBasketType);
                            if (tourFromDemands != null)
                                timeSlot.addTours(tourFromDemands);

                        }));

    }
}