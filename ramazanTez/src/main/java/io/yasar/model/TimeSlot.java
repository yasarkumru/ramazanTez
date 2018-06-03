package io.yasar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeSlot {

	private final Integer rank;
	private final List<Tour> tours;

	public TimeSlot(Integer rank) {
		this.rank = rank;
		this.tours = new ArrayList<>();
	}

	public TimeSlot(TimeSlot timeSlot) {
		this.rank = timeSlot.rank;
		this.tours = timeSlot.getTours().stream()
				.map(Tour::new)
				.collect(Collectors.toList());
	}

	public Integer getRank() {
		return rank;
	}

	public List<Tour> getTours() {
		return new ArrayList<>(tours);
	}

	/**
	 * this method not just adds the tour
	 * 
	 * @param tour
	 */
	public void mergeTour(Tour tour) {
		tours.stream()
				.filter(t -> t.getBasketType().equals(tour.getBasketType()))
				.filter(t -> !t.isFull())
				.forEach(t -> t.merge(tour));

		if (!tour.isEmpty())
			tours.add(tour);
	}

	public void addTours(List<Tour> toursToAdd) {
		toursToAdd.stream().forEach(this::mergeTour);
	}

	public int getLineSideDif() {
		return tours.stream()
				.mapToInt(tour -> tour.getDemands().stream()
						.mapToInt(demand -> (demand.getTimeSlot().getRank() - rank)
								* demand.getBasketCount())
						.sum())
				.sum();
	}

	public int getTourCount() {
		return getTours().stream()
				.mapToInt(tour -> tour.getDemands().size() > 0 ? 1 : 0)
				.sum();
	}

	public void removeTour(Tour tour) {
		if (!tours.remove(tour))
			throw new RuntimeException("Cannot find specified tour in time slot!!!");
	}

	@Override
	public String toString() {
		return "TimeSlot: " + getTourCount();
	}

}
