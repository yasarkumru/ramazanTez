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

	public String getName() {
		return name;
	}

	public BasketType getBasketType() {
		return basketType;
	}
	
	@Override
	public String toString() {
		
		return "[id: "+ id +", name: "+name +", basketType: "+basketType+"]";
	}

}
