package ramazanTez;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.yasar.RamcoTezMain;
import io.yasar.model.Demand;
import io.yasar.model.Product;
import io.yasar.repository.DemandRepository;
import io.yasar.repository.ProductRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RamcoTezMain.class})
public class ExcelTests {
	
	@Autowired ProductRepository productDao;
	@Autowired DemandRepository demandDao;

	@Test
	public void productConstruct() {
		List<Product> constructProducts = productDao.getProducts();
		assertEquals(108, constructProducts.size());
	}
	
	@Test
	public void demandConstruct(){
		final List<Demand> demands = demandDao.getDemands();
		assertEquals(220, demands.size());
	}

}
