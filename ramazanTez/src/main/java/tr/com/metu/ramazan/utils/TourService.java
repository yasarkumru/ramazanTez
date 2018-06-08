package tr.com.metu.ramazan.utils;

import java.util.ArrayList;
import java.util.List;

import tr.com.metu.ramazan.model.BasketType;
import tr.com.metu.ramazan.model.Demand;
import tr.com.metu.ramazan.model.Tour;

public final class TourService {
    
    private TourService() {}

	public static List<Tour> getToursFromDemands(List<Demand> demands) {
		if(demands.isEmpty())
			return new ArrayList<>();
		List<Tour> tours = new ArrayList<>();
		BasketType basketType = demands.get(0).getBasketType();
		demands.sort((d1, d2) -> (int) (d2.getValue() - d1.getValue()));
		
		Tour currentTour = new Tour(basketType);
		while (!demands.isEmpty()) {
			Demand demand = demands.get(0);
			if(currentTour.isAddable(demand)){
				currentTour.addDemand(demand);
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
