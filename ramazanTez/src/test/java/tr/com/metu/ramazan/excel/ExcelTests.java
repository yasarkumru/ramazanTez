package tr.com.metu.ramazan.excel;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tr.com.metu.ramazan.RamazanTezMain;
import tr.com.metu.ramazan.model.Demand;
import tr.com.metu.ramazan.model.Product;
import tr.com.metu.ramazan.repository.DemandRepository;
import tr.com.metu.ramazan.repository.ProductRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RamazanTezMain.class})
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
