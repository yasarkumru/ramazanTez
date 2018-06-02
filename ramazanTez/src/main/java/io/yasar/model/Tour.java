package io.yasar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
		this.demands = new ArrayList<>(tour.getDemands());
	}

	public void addDemand(Demand demand) {
		demands.add(demand);
	}

	public void removeDemand(Demand demand) {
		if (!demands.remove(demand))
			throw new RuntimeException("Cannot remove demand from specified tour");
	}

	public int getMaxCarriedBasketSize() {
		return basketType.getMaxCarriedSize();
	}

	public int getBasketCount() {
		return demands.stream().mapToInt(Demand::getBasketCount).sum();
	}

	public BasketType getBasketType() {
		return basketType;
	}

	public boolean isFull() {
		return getBasketCount() == getMaxCarriedBasketSize();
	}

	public List<Demand> getDemands() {
		return demands;
	}

	public boolean addable(Demand demand) {
		if (demand.getProduct().getBasketType() != basketType)
			return false;

		if (this.getBasketCount() + demand.getBasketCount() > getMaxCarriedBasketSize())
			return false;
		return true;
	}

	public boolean isFractional() {
		final boolean isFractional = getBasketCount() < this.getMaxCarriedBasketSize();
		if(isFractional)
			System.out.println("FRACTIONAL TOUR FOUND");
		return isFractional;
	}

	public void merge(Tour tour) {
		if (!this.getBasketType().equals(tour.getBasketType()))
			throw new RuntimeException("Cannot merge different type of basket types");

		if (this.isFull())
			return;

		List<Demand> demands2 = tour.getDemands();
		while (!demands2.isEmpty() && !this.isFull()) {
			Demand remove = demands2.remove(0);
			if (addable(remove)) {
				this.addDemand(remove);
				tour.removeDemand(remove);
			}
		}

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
	public String toString() {
		
		return "Tour: "+getBasketCount();
	}

}
