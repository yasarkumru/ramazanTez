package tr.com.metu.ramazan.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import tr.com.metu.ramazan.model.BasketType;

@Component
public class BasketTypeRepository {

	private List<BasketType> basketTypes;

	@PostConstruct
	private void init() {
		basketTypes = new ArrayList<>();

		basketTypes.add(new BasketType(1, 6));
		basketTypes.add(new BasketType(2, 4));
		basketTypes.add(new BasketType(3,3));
		basketTypes.add(new BasketType(4, 2));
	}

	public BasketType findBasketTypeById(Integer id) {
		return basketTypes.stream()
		        .filter(type -> type.getId() == id)
		        .findFirst().get();
	}
	
	public List<BasketType> getBasketTypes() {
		return basketTypes;
	}

}
