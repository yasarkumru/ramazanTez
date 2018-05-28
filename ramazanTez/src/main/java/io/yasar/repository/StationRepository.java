package io.yasar.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import io.yasar.excel.Constants;
import io.yasar.model.Station;

@Component
public class StationRepository {

	
	private List<Station> stations;
	
	@PostConstruct
	public void init(){
		stations = IntStream
				.range(0, Constants.STATION_COUNT)
				.mapToObj(index -> new Station(index))
				.collect(Collectors.toList());
		
	}

	public Station findStationById(Integer id) {
		return stations.stream()
				.filter(station -> station.getId().equals(id))
				.findFirst().get();

	}

}
