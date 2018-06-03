package io.yasar.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.yasar.excel.Constants;

/**
 *
 * @author yasar
 */
public class Solution {

	private final List<TimeSlot> timeSlots;

	public Solution(List<TimeSlot> timeSlots) {
		super();
		this.timeSlots = timeSlots;
	}

	public Solution(Solution solution) {
		super();
		this.timeSlots = solution.timeSlots.stream()
				.map(TimeSlot::new)
				.collect(Collectors.toList());
	}

	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}

	public boolean isFeasible() {
		// skip 0th time slot
		return timeSlots.stream().skip(1)
				.allMatch(timeSlot -> timeSlot.getTourCount() <= Constants.MAX_TOUR_PER_TIME_SLOT);
	}

	public TimeSlot getTheWorstTimeSlot() {
		return timeSlots.stream().skip(1).sorted((t1, t2) -> t1.getTourCount() == t2.getTourCount()
				? t2.getRank() - t1.getRank()
				: t2.getTourCount() - t1.getTourCount())
				.findFirst().get();
	}

	public int getTotalLineSideDif() {
		return timeSlots.stream().mapToInt(timeSlot -> timeSlot.getLineSideDif()).sum();
	}

	public int getTotalTourCount() {
		return timeSlots.stream().mapToInt(timeSlot -> timeSlot.getTourCount()).sum();
	}

	public int findIndexOfTimeSlot(TimeSlot timeSlot) {
		for (int i = 0; i < timeSlots.size(); i++) {
			if (timeSlot == timeSlots.get(i))
				return i;
		}
		throw new RuntimeException("Couldn't find TimeSlot in Solution");
	}

	private TimeSlot findTimeSlotOfTour(Tour tour) {
		Optional<TimeSlot> findFirst = timeSlots
				.stream()
				.filter(timeSlot -> timeSlot.getTours().contains(tour))
				.findFirst();
		return findFirst.get();
	}

	public boolean isFinished() {
		return timeSlots.stream().skip(1).flatMap(ts -> ts.getTours().stream())
				.allMatch(to -> to.isEmpty());
	}

	/**
	 * this method is to make sure sliding is working immutable
	 * 
	 * @param tour
	 * @return
	 */
	public Solution slideTour(Tour tour) {
		return new Solution(this)._slideTour(new Tour(tour));
	}

	private Solution _slideTour(Tour tour) {
		TimeSlot timeSlotFrom = findTimeSlotOfTour(tour);
		int index = findIndexOfTimeSlot(timeSlotFrom);
		TimeSlot timeSlotTo = getTimeSlots().get(index - 1);
		doSlide(timeSlotFrom, timeSlotTo, tour);
		return this;
	}

	private static void doSlide(TimeSlot from, TimeSlot to, Tour tour) {
		from.removeTour(tour);
		to.mergeTour(tour);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Solution: " + isFeasible() + "\n");
		sb.append("Tour Count: " + getTotalTourCount())
		.append(" Line side dif: " + getTotalLineSideDif())
		.append("\n");

		timeSlots.forEach(
				ts -> {
					ts.getTours().forEach(t -> sb
							.append(t.getBasketCount())
							.append("  "));
					sb.append("\n");
				});

		return sb.toString();
	}
}
