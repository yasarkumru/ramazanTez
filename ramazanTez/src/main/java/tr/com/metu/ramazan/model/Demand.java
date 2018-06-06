package tr.com.metu.ramazan.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this entity also act as basket
 * @author yasar
 *
 */
public class Demand {

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	private final int id;
	private final Product product;
	private final Station station;
	private final TimeSlot timeSlot;
	private Double value;

	public Demand(Product product, Station station, Double value, TimeSlot timeSlot) {
		super();
		this.product = product;
		this.station = station;
		this.value = value;
		this.timeSlot = timeSlot;
		this.id = ID_GENERATOR.incrementAndGet();
	}
	
	public Demand(Demand demand){
		this.id = demand.id;
		this.product = demand.product;
		this.station = demand.station;
		this.timeSlot = demand.timeSlot;
		this.value = demand.value;
	}

	private Demand(Demand demand, Double value) {
		super();
		this.product = demand.product;
		this.station = demand.station;
		this.value = value;
		this.timeSlot = demand.timeSlot;
		this.id = ID_GENERATOR.incrementAndGet();
	}

	public Product getProduct() {
		return product;
	}

	public Station getStation() {
		return station;
	}

	public Double getValue() {
		return value;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	@Override
	public String toString() {
		return "[Product: " + product.toString()  + ",value: "
				+ value + "]";
	}

	public int getBasketCount() {
		return (int) Math.ceil(value);
	}

	public void merge(Demand demand2) {
		if (!this.getProduct().equals(demand2.getProduct()))
			return;
		if (!this.getStation().equals(demand2.getStation()))
			return;

		if (this.getRemainingValueForNextBasket() >= demand2.getLeftOverSize()) {
			this.value += demand2.getLeftOverSize();
			demand2.value -= demand2.getLeftOverSize();
		}
	}

	public List<Demand> splitDemand() {
		Double demandVal = getValue();
		List<Demand> splittedDemands = new ArrayList<>();
		while (demandVal > 0) {
			if (demandVal < 1) {
				splittedDemands.add(new Demand(this, demandVal));
				demandVal = 0d;
			} else {
				splittedDemands.add(new Demand(this, 1d));
				demandVal -= 1;
			}

		}
		return splittedDemands;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Demand))
			return false;
		Demand demand = (Demand) obj;
		return this.id == demand.id;
	}

	private Double getLeftOverSize() {
		return value - value.intValue();
	}

	private Double getRemainingValueForNextBasket() {
		return 1d - getLeftOverSize();
	}

	public BasketType getBasketType() {
		return product.getBasketType();
	}

}
