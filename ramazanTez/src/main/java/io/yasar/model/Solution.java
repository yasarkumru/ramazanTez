package io.yasar.model;

import java.util.ArrayList;
import java.util.List;

import io.yasar.excel.Constants;

/**
 *
 * @author yasar
 */
public class Solution implements Cloneable {

    private List<TimeSlot> timeSlots;

    public Solution(List<TimeSlot> timeSlots) {
        super();
        this.timeSlots = timeSlots;
    }

    @SuppressWarnings("unchecked")
    private Solution(Solution solution) {
        super();
        this.timeSlots = (List<TimeSlot>) ((ArrayList<TimeSlot>) solution.timeSlots).clone();
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Solution(this);
    }

    public boolean isFeasible() {
        // skip 0th time slot
        return timeSlots.stream().skip(1)
                .anyMatch(timeSlot -> timeSlot.getTourCount() <= Constants.MAX_TOUR_PER_TIME_SLOT);
    }

    public TimeSlot getTheWorstTimeSlot() {
        return timeSlots.stream().sorted((t1, t2) -> t1.getTourCount() == t2.getTourCount()
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

    public int getIndexOfTimeSlot(TimeSlot timeSlot) {
        for (int i = 0; i < timeSlots.size(); i++) {
            if (timeSlot == timeSlots.get(i))
                return i;
        }
        throw new RuntimeException("Couldn't find TimeSlot in Solution");
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

    
    private Solution _slideTour(Tour tour) {
        return this;
    }

}
