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
	
	public void removeDemand(Demand demand) {
	    if(!demands.remove(demand))
	        throw new RuntimeException("Cannot remove demand from specified tour");
	}

	public int getMaxCarriedBasketSize() {
		return basketType.getMaxCarriedSize();
	}

	public int getBasketSize() {
		return demands.stream().mapToInt(Demand::getBasketSize).sum();
	}
	
	public BasketType getBasketType() {
        return basketType;
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

		if (this.getBasketSize() + demand.getBasketSize() > getMaxCarriedBasketSize())
			return false;
		return true;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Tour(this);
	}
	
	public boolean isFractional() {
	    return getBasketSize() < this.getMaxCarriedBasketSize();
	}

    public void merge(Tour tour) {
        if(!this.getBasketType().equals(tour.getBasketType()))
            throw new RuntimeException("Cannot merge different type of basket types");
        
        if(this.isFull())
            return;
        
        List<Demand> demands2 = tour.getDemands();
        while (!demands2.isEmpty() && !this.isFull()) {
            Demand remove = demands2.remove(0);
            if(addable(remove)) {
                this.addDemand(remove);
                tour.removeDemand(remove);
            }
        }
        
    }

    public boolean isEmpty() {
        return this.getBasketSize() == 0;
    }
	
	

}
