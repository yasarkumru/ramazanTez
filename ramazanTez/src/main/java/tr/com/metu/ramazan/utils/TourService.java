package tr.com.metu.ramazan.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import tr.com.metu.ramazan.model.BasketType;
import tr.com.metu.ramazan.model.Demand;
import tr.com.metu.ramazan.model.Tour;

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
			if(currentTour.isMergable(demand)){
				currentTour.mergeDemand(demand);
				demands.remove(demand);
				if(demands.isEmpty())
					tours.add(currentTour);
			}else{
				tours.add(currentTour);
				currentTour = new Tour(basketType);
			}
				
		}
		return tours;
	}
	
	

}