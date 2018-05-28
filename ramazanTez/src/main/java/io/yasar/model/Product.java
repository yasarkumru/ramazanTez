package io.yasar.model;

public class Product {

	private Integer id;
	private String name;
	private BasketType basketType;

	public Product(Integer id, String name, BasketType basketType) {
		super();
		this.id = id;
		this.name = name;
		this.basketType = basketType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BasketType getBasketType() {
		return basketType;
	}

	public void setBasketType(BasketType basketType) {
		this.basketType = basketType;
	}
	
	@Override
	public String toString() {
		
		return "[id: "+ id +", name: "+name +", basketType: "+basketType+"]";
	}

}
