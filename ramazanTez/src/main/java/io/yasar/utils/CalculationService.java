package io.yasar.utils;

import java.util.List;

import org.springframework.stereotype.Service;

import io.yasar.model.TimeSlot;

@Service
public class CalculationService {

	private final TimeSlotService timeSlotService;

	public CalculationService(TimeSlotService timeSlotService) {
		this.timeSlotService = timeSlotService;
	}

	public List<TimeSlot> run(List<TimeSlot> timeSlots) {
		// TODO
		List<TimeSlot> nextStep = null;
		List<TimeSlot> currentStep = timeSlots;

		while (true) {
			nextStep = nextStep(currentStep);
			if (!isChanged(currentStep, nextStep))
				break;
		}

		return currentStep;
	}

	public List<TimeSlot> nextStep(List<TimeSlot> timeSlots) {
		TimeSlot worstTimeSlot = TimeSlotService.findTheWorstTimeSlot(timeSlots);
		return TimeSlotService
				.trySlidingToursInTimeSlot(timeSlots, worstTimeSlot);
	}

	private boolean isChanged(List<TimeSlot> timeSlots, List<TimeSlot> nextStep) {
		// TODO Auto-generated method stub
		return false;
	}

}
