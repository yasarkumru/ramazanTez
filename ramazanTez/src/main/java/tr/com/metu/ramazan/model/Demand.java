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
	private TimeSlot lastTimeSlot;
	private Double value;

	private Demand(Product product, Station station, Double value, TimeSlot timeSlot,TimeSlot lastTimeSlot, int id) {
		this.product = product;
		this.station = station;
		this.value = value;
		this.timeSlot = timeSlot;
		this.lastTimeSlot = lastTimeSlot;
		this.id = id;
	}

	
	public Demand(Product product, Station station, Double value, TimeSlot timeSlot) {
		this(product,station,value,timeSlot,timeSlot,ID_GENERATOR.incrementAndGet());
	}
	
	public Demand(Demand demand){
		this(demand.product,demand.station,demand.value,demand.timeSlot,demand.lastTimeSlot,demand.id);
	}

	private Demand(Demand demand, TimeSlot lastTimeSlot, Double value) {
		this(demand.product,demand.station,value,demand.timeSlot,lastTimeSlot,ID_GENERATOR.incrementAndGet());
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

	public TimeSlot getLastTimeSlot() {
		return lastTimeSlot;
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

		if (this.getRemainingValueForNextBasket() >0 && demand2.getLeftOverSize() >0) {
			double moveValue = Math.min(this.getRemainingValueForNextBasket(), demand2.getLeftOverSize());
			this.value += moveValue;
			demand2.value -= moveValue;
			if(demand2.getLastTimeSlot().getRank() > this.getLastTimeSlot().getRank()){
				this.lastTimeSlot = demand2.lastTimeSlot;
			}
		}
	}

	public List<Demand> splitDemand() {
		Double demandVal = getValue();
		List<Demand> splittedDemands = new ArrayList<>();
		while (demandVal > 0) {
			if (demandVal < 1) {
				splittedDemands.add(new Demand(this,this.getTimeSlot(), new Double(demandVal)));
				demandVal = 0d;
			} else {
				splittedDemands.add(new Demand(this,this.getLastTimeSlot(), 1d));
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
		final double left = value - value.intValue();
		if(left == 0)
			return 1d;
		return left;
	}

	private Double getRemainingValueForNextBasket() {
		return 1d - getLeftOverSize();
	}

	public BasketType getBasketType() {
		return product.getBasketType();
	}

}
