package tr.com.metu.ramazan.model;

public class BasketType {

	private Integer id;
	private Integer maxCarriedSize;

	public BasketType(Integer id, Integer maxCarriedSize) {
		this.id = id;
		this.maxCarriedSize = maxCarriedSize;
	}

	public Integer getId() {
		return id;
	}

	public Integer getMaxCarriedSize() {
		return maxCarriedSize;
	}
}
