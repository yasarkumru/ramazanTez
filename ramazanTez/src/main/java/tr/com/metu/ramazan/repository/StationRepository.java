package tr.com.metu.ramazan.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import tr.com.metu.ramazan.excel.Constants;
import tr.com.metu.ramazan.model.Station;

@Component
public class StationRepository {

    private List<Station> stations;

    @PostConstruct
    public void init() {
        stations = IntStream
                .range(0, Constants.STATION_COUNT)
                .mapToObj(Station::new)
                .collect(Collectors.toList());

    }

    public Station findStationById(Integer id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't find station by id: " + id));

    }

}
