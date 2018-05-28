package io.yasar.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.yasar.model.Demand;


@Service
public final class DemandService {

	public static List<Demand> mergeAll(List<Demand> demands) {
		for (int i = 0; i < demands.size(); i++) {
			Demand demand1 = demands.get(i);
			for (int j = i + 1; j < demands.size(); j++) {
				Demand demand2 = demands.get(j);
				demand1.merge(demand2);
			}

		}
		return demands.stream().filter(demand -> demand.getValue() > 0)
				.collect(Collectors.toList());
	}
}
