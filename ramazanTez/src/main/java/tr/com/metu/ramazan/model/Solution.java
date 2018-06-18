package tr.com.metu.ramazan.model;

import java.util.List;
import java.util.stream.Collectors;

import tr.com.metu.ramazan.excel.Constants;
import tr.com.metu.ramazan.exception.NotFoundException;

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
        return timeSlots.stream()
                .skip(1)
                .allMatch(timeSlot -> timeSlot.getTourCount() <= Constants.MAX_TOUR_PER_TIME_SLOT);
    }

    public TimeSlot getTheWorstTimeSlot() {
        return timeSlots.stream()
                .skip(2)
                .sorted((t1, t2) -> t1.getTourCount() == t2.getTourCount()
                        ? t2.getRank() - t1.getRank()
                        : t2.getTourCount() - t1.getTourCount())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The worst time slot couldn't be found"));
    }

    public int getTotalLineSideDif() {
        return timeSlots.stream()
                .mapToInt(TimeSlot::getLineSideDif)
                .sum();
    }

    public int getTotalTourCount() {
        return timeSlots.stream()
                .mapToInt(TimeSlot::getTourCount)
                .sum();
    }

    public int findIndexOfTimeSlot(TimeSlot timeSlot) {
        for (int i = 0; i < timeSlots.size(); i++) {
            if (timeSlot == timeSlots.get(i))
                return i;
        }
        throw new NotFoundException("Couldn't find TimeSlot in Solution");
    }

    private TimeSlot findTimeSlotOfTour(Tour tour) {
        return timeSlots
                .stream()
                .filter(timeSlot -> timeSlot.getTours().contains(tour))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("The tour is not belong to this timeSlot!!"));
    }

    /**
     * this method is to make sure sliding is working immutable
     * 
     * @param tour
     * @return
     */
    public Solution slideTour(Tour tour) {
        return new Solution(this).slideTourPrivate(new Tour(tour));
    }

    private Solution slideTourPrivate(Tour tour) {
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
                    ts.getTours().forEach(t -> {
                        sb
                                .append("|" + t.getBasketCount() + "> ");
                        t.getDemands().forEach(
                                d -> sb.append(d.getLastTimeSlot().getRank() + "=" + d.getTimeSlot().getRank() + " "));
                    });
                    sb.append("\n");
                });

        return sb.toString();
    }

    public int getPriorityHeuristic() {
        return this.getTotalTourCount() * 100
                + this.getTotalLineSideDif() * 10
                + timeSlots.stream().mapToInt(ts -> ts.getTourCount() * ts.getRank()).sum();
    }
}
