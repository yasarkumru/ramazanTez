package io.yasar.model;

import java.util.ArrayList;
import java.util.List;

public class Tour implements Cloneable{

	private BasketType basketType;
	private List<Demand> demands = new ArrayList<>();

	public Tour(BasketType basketType) {
		this.basketType = basketType;

	}

	@SuppressWarnings("unchecked")
	private Tour(Tour tour) {
		this.basketType = tour.basketType;
		this.demands = (List<Demand>) ((ArrayList<Demand>)tour.demands).clone();
	}

	public void addDemand(Demand demand) {

		demands.add(demand);
	}

	public int getMaxCarriedBasketSize() {
		return basketType.getMaxCarriedSize();
	}

	public int getBasketSize() {
		return demands.stream().mapToInt(Demand::getBasketSize).sum();
	}

	public boolean isFull() {
		return getBasketSize() == getMaxCarriedBasketSize();
	}
	
	public List<Demand> getDemands() {
		return demands;
	}

	public boolean addable(Demand demand) {
		if (demand.getProduct().getBasketType() != basketType)
			return false;

		if (getBasketSize() + demand.getBasketSize() > getMaxCarriedBasketSize())
			return false;
		return true;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Tour(this);
	}
	
	

}
