package io.yasar.model;

import java.util.ArrayList;
import java.util.List;

public class TimeSlot implements Cloneable {

    private final Integer rank;
    private List<Tour> tours = new ArrayList<>();

    public TimeSlot(Integer rank) {
        this.rank = rank;
    }

    @SuppressWarnings("unchecked")
    private TimeSlot(TimeSlot timeSlot) {
        this.rank = timeSlot.rank;
        this.tours = (List<Tour>) ((ArrayList<Tour>) timeSlot.tours).clone();
    }

    public Integer getRank() {
        return rank;
    }

    public List<Tour> getTours() {
        return tours;
    }

    /**
     * this method not just adds the tour 
     * @param tour
     */
    public void addTour(Tour tour) {
        tours.stream().filter(t -> t.getBasketType().equals(tour.getBasketType()))
                .filter(t -> !t.isFull())
                .forEach(t -> t.merge(tour));

        if (tour.getBasketSize() > 0)
            tours.add(tour);
    }

    public void addTours(List<Tour> toursToAdd) {
        toursToAdd.stream().forEach(this::addTour);
    }

    public int getLineSideDif() {
        return tours.stream()
                .mapToInt(tour -> tour.getDemands().stream()
                        .mapToInt(demand -> (rank - demand.getTimeSlot().getRank())
                                * demand.getBasketSize())
                        .sum())
                .sum();
    }

    public int getTourCount() {
        return getTours().size();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new TimeSlot(this);
    }

    public void removeTour(Tour tour) {
        if (!tours.remove(tour))
            throw new RuntimeException("Cannot find specified tour in time slot!!!");
    }

}
