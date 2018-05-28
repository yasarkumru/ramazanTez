package io.yasar.model;

import java.util.ArrayList;
import java.util.List;

public class TimeSlot implements Cloneable{

	private final Integer rank;
	private List<Tour> tours = new ArrayList<>();

	public TimeSlot(Integer rank) {
		this.rank = rank;
	}

	@SuppressWarnings("unchecked")
	private TimeSlot(TimeSlot timeSlot) {
		this.rank = timeSlot.rank;
		this.tours = (List<Tour>) ((ArrayList<Tour>)timeSlot.tours).clone();
	}

	public Integer getRank() {
		return rank;
	}

	public List<Tour> getTours() {
		return tours;
	}

	public void addTour(Tour tour) {
		tours.add(tour);
	}

	public void addTours(List<Tour> toursToAdd) {
		tours.addAll(toursToAdd);
	}

	public int getLineSideDif() {
		return tours.stream()
				.mapToInt(tour -> tour.getDemands().stream()
						.mapToInt(demand -> (rank - demand.getTimeSlot().getRank())
								* demand.getBasketSize())
						.sum())
				.sum();
	}
	
	public int getTourCount(){
		return getTours().size();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new TimeSlot(this);
	}

}
