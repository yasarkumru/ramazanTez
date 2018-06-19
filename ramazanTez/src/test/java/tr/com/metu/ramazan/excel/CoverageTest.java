package tr.com.metu.ramazan.excel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tr.com.metu.ramazan.RamazanTezMain;
import tr.com.metu.ramazan.commands.Commands;
import tr.com.metu.ramazan.model.Solution;
import tr.com.metu.ramazan.repository.TimeSlotRepository;
import tr.com.metu.ramazan.utils.CalculationService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RamazanTezMain.class})
public class CoverageTest {
	
	@Autowired TimeSlotRepository timeSlotRepository;
	@Autowired Commands commands;

	@Test
	public void productConstruct() {
	    List<Solution> run = CalculationService.run(new Solution(timeSlotRepository.getTimeSlots()));
	    
	    commands.run();
	    
	    run.get(0).toString();
	    
		assertNotNull(run);
		assertTrue(run.size()>0);
	}
	

}
