package io.yasar.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import io.yasar.model.BasketType;

@Component
public class BasketTypeRepository {

	private List<BasketType> basketTypes;

	@PostConstruct
	private void init() {
		basketTypes = new ArrayList<>();

		basketTypes.add(new BasketType(1, 6));
		basketTypes.add(new BasketType(2, 6));
	}

	public BasketType findBasketTypeById(Integer id) {
		return basketTypes.stream().filter(type -> type.getId() == id).findFirst().get();
	}
	
	public List<BasketType> getBasketTypes() {
		return basketTypes;
	}

}
