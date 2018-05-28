package io.yasar.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import io.yasar.model.TimeSlot;
import io.yasar.model.Tour;

@Service
public final class TimeSlotService {

//	private final BasketTypeRepository basketTypeRepository;
//	
//	public TimeSlotService(BasketTypeRepository basketTypeRepository) {
//		this.basketTypeRepository = basketTypeRepository;
//	}
	
	
	public static TimeSlot findTheWorstTimeSlot(List<TimeSlot> timeSlots) {
		return timeSlots.stream().sorted((t1, t2) -> {
			if (t1.getTourCount() == t2.getTourCount())
				return t2.getRank() - t1.getRank();
			return t2.getTourCount() - t1.getTourCount();
		}).findFirst().get();
	}

	public static List<TimeSlot> trySlidingToursInTimeSlot(List<TimeSlot> timeSlots,
			TimeSlot timeSlot) {

		int currentDif = getTotalLineSideDif(timeSlots);
		int indexOfTimeSlot = findIndexOfTimeSlot(timeSlots, timeSlot);
		List<TimeSlot> bestSolution = timeSlots;
		List<Tour> tours = timeSlot.getTours();
		for (int i = 0; i < tours.size(); i++) {
			List<TimeSlot> cloneTimeSlots = cloneTimeSlots(timeSlots);
			Tour tour = tours.get(i);
			slide(cloneTimeSlots,timeSlot,tour);
			int dif = getTotalLineSideDif(cloneTimeSlots);
			
		}
		
		
		return null;
	}
	
	private static void slide(List<TimeSlot> timeSlots, TimeSlot timeSlot, Tour tour){
		//TODO
	}
	
	
	@SuppressWarnings("unchecked")
	private static List<TimeSlot> cloneTimeSlots(List<TimeSlot> timeSlots){
		return (List<TimeSlot>) ((ArrayList<TimeSlot>) timeSlots).clone();
	}

	private static int findIndexOfTimeSlot(List<TimeSlot> timeSlots, TimeSlot timeSlot) {
		for (int i = 0; i < timeSlots.size(); i++) {
			if (timeSlot.equals(timeSlots.get(i)))
				return i;
		}
		return 0;
	}

	public static int getTotalLineSideDif(List<TimeSlot> timeSlots) {
		return timeSlots.stream().mapToInt(timeSlot -> timeSlot.getLineSideDif()).sum();
	}
}
