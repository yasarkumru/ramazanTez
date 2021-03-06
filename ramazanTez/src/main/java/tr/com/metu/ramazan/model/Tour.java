package tr.com.metu.ramazan.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import tr.com.metu.ramazan.exception.NotFoundException;

public class Tour {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private final int id;
    private final BasketType basketType;
    private final List<Demand> demands;

    public Tour(BasketType basketType) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.basketType = basketType;
        this.demands = new ArrayList<>();

    }

    public Tour(Tour tour) {
        this.id = tour.id;
        this.basketType = tour.basketType;
        this.demands = tour.getDemands().stream()
                .map(Demand::new)
                .collect(Collectors.toList());
    }

    public int getMaxCarriedBasketSize() {
        return basketType.getMaxCarriedSize();
    }

    public int getBasketCount() {
        return demands.stream()
                .mapToInt(Demand::getBasketCount)
                .sum();
    }

    public BasketType getBasketType() {
        return basketType;
    }

    public boolean isFull() {
        return getBasketCount() == getMaxCarriedBasketSize();
    }

    public List<Demand> getDemands() {
        return new ArrayList<>(demands);
    }

    public boolean isAddable(Demand demand) {
        if (!demand.getProduct().getBasketType().equals(basketType))
            return false;
        return this.getBasketCount() + demand.getBasketCount() <= getMaxCarriedBasketSize();
    }

    public boolean isFractional() {
        return getBasketCount() < this.getMaxCarriedBasketSize();
    }

    public void merge(Tour tour) {
        if (!this.getBasketType().equals(tour.getBasketType()))
            throw new NotFoundException("Cannot merge different type of basket types");

        List<Demand> demandsToMerge = tour.getDemands();
        while (!demandsToMerge.isEmpty() && !this.isFull()) {
            Demand demandToMerge = demandsToMerge.get(0);
            if (isAddable(demandToMerge)) {
                this.addDemand(demandToMerge);
                tour.removeDemand(demandToMerge);
                demandsToMerge.remove(demandToMerge);
            }
        }

    }

    public void addDemand(Demand demand) {
        demands.add(demand);
    }

    public void removeDemand(Demand demand) {
        if (!demands.remove(demand))
            throw new NotFoundException("Cannot remove demand from specified tour");
    }

    public boolean isEmpty() {
        return this.getBasketCount() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tour))
            return false;
        Tour t = (Tour) obj;
        return this.id == t.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
