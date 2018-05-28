package io.yasar.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import io.yasar.model.BasketType;
import io.yasar.model.Demand;
import io.yasar.model.Tour;

@Service
public final class TourService {

	public static List<Tour> getToursFromDemands(List<Demand> demands) {
		if(demands.isEmpty())
			return null;
		List<Tour> tours = new ArrayList<>();
		BasketType basketType = demands.get(0).getBasketType();
		demands.sort((d1, d2) -> (int) (d2.getValue() - d1.getValue()));
		
		Tour currentTour = new Tour(basketType);
		while (!demands.isEmpty()) {
			Demand demand = demands.get(0);
			if(currentTour.addable(demand)){
				currentTour.addDemand(demand);
				demands.remove(demand);
			}else{
				tours.add(currentTour);
				currentTour = new Tour(basketType);
			}
				
		}
		return tours;
	}
	
	

}
