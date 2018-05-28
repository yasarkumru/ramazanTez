package io.yasar.commands;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import io.yasar.model.Demand;
import io.yasar.model.TimeSlot;
import io.yasar.model.Tour;
import io.yasar.repository.BasketTypeRepository;
import io.yasar.repository.DemandRepository;
import io.yasar.repository.TimeSlotRepository;
import io.yasar.utils.CalculationService;
import io.yasar.utils.TimeSlotService;
import io.yasar.utils.TourService;

@ShellComponent
public class Commands {

	private final DemandRepository demandRepository;
	private final TimeSlotRepository timeSlotRepository;
	private final BasketTypeRepository basketTypeRepository;
	private final CalculationService calculationService;

	public Commands(DemandRepository demandRepository,
			TimeSlotRepository timeSlotRepository,
			BasketTypeRepository basketTypeRepository,
			CalculationService calculationService) {
		this.demandRepository = demandRepository;
		this.timeSlotRepository = timeSlotRepository;
		this.basketTypeRepository = basketTypeRepository;
		this.calculationService = calculationService;
	}

	@ShellMethod("Runs the algorithm")
	public void run() {
		List<TimeSlot> run = calculationService.run(timeSlotRepository.getTimeSlots());
		System.out.println(run);
	}
	
	@PostConstruct
	private void init(){
		//add demands to timeSlots
		timeSlotRepository.getTimeSlots().stream().forEach(timeSlot -> {
			basketTypeRepository.getBasketTypes().stream().forEach(basketType -> {
				List<Demand> findDemandsByTimeSlotAndBasketType = demandRepository
						.findDemandsByTimeSlotAndBasketType(timeSlot, basketType);

				List<Tour> tourFromDemands = TourService
						.getToursFromDemands(findDemandsByTimeSlotAndBasketType);
				if (tourFromDemands != null)
					timeSlot.addTours(tourFromDemands);

			});
		});

	}
}