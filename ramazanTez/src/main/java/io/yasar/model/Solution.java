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
        return !timeSlots.stream().anyMatch(timeSlot -> timeSlot.getTourCount() > Constants.MAX_TOUR_PER_TIME_SLOT);
    }

    public TimeSlot getTheWorstTimeSlot() {
        return timeSlots.stream().sorted((t1, t2) -> {
            if (t1.getTourCount() == t2.getTourCount())
                return t2.getRank() - t1.getRank();
            return t2.getTourCount() - t1.getTourCount();
        }).findFirst().get();
    }

    public int getTotalLineSideDif() {
        return timeSlots.stream().mapToInt(timeSlot -> timeSlot.getLineSideDif()).sum();
    }

    public int getTotalTourCount() {
        return timeSlots.stream().mapToInt(timeSlot -> timeSlot.getTourCount()).sum();
    }

}
