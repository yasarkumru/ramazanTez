package io.yasar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.yasar.excel.Constants;

/**
 *
 * @author yasar
 */
public class Solution implements Cloneable, Comparable<Solution> {

    private List<TimeSlot> timeSlots;

    public Solution(List<TimeSlot> timeSlots) {
        super();
        this.timeSlots = timeSlots;
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

    public TimeSlot findTimeSlotOfTour(Tour tour) {
        Optional<TimeSlot> findFirst = timeSlots
                .stream()
                .filter(timeSlot -> timeSlot.getTours().contains(tour))
                .findFirst();
        
        return findFirst.get();
    }

    /**
     * this method is to make sure sliding is working immutable
     * 
     * @param tour
     * @return
     */
    public Solution slideTour(Tour tour) {
        try {
            return ((Solution) clone())._slideTour(tour);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compareTo(Solution o) {
        return o.getTotalLineSideDif() - this.getTotalLineSideDif();
    }

    public boolean isFinished() {
        return timeSlots.stream().skip(1).flatMap(ts -> ts.getTours().stream()).allMatch(to -> to.isEmpty());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Solution(this);
    }

    @SuppressWarnings("unchecked")
    private Solution(Solution solution) {
        super();
        this.timeSlots = (List<TimeSlot>) ((ArrayList<TimeSlot>) solution.timeSlots).clone();
    }

    private Solution _slideTour(Tour tour) {
        TimeSlot timeSlotFrom = findTimeSlotOfTour(tour);
        int index = findIndexOfTimeSlot(timeSlotFrom);
        if (index == 0)
            return null;
    
        TimeSlot timeSlotTo = getTimeSlots().get(index - 1);
        doSlide(timeSlotFrom, timeSlotTo, tour);
        return this;
    }

    private static void doSlide(TimeSlot from, TimeSlot to, Tour tour) {
        from.removeTour(tour);
        to.addTour(tour);
    }

}
