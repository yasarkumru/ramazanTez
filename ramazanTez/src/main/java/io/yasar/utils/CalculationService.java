package io.yasar.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.yasar.model.Solution;
import io.yasar.model.TimeSlot;
import io.yasar.model.Tour;

@Service
public class CalculationService {

	public List<Solution> run(Solution solution) {

		List<Solution> solutions = new ArrayList<>();
		List<Solution> allCurrents = new ArrayList<>();
		Solution current = new Solution(solution);

		while (true) {
			allCurrents.add(current);
			if (current.isFeasible()) {
				if (current.isFinished())
					break;
				solutions.add(current);
				Optional<Solution> nextSolution = current.getTimeSlots()
						.stream()
						.skip(1)
						.flatMap(ts -> ts.getTours().stream())
						.filter(Tour::isFractional)
						.map(current::slideTour)
						.sorted((s1, s2) -> {
							return s1.getTotalLineSideDif() - s2.getTotalLineSideDif();
						})
						.findFirst();
				if (!nextSolution.isPresent())
					break;

				current = nextSolution.get();

			} else {// if not feasible
				TimeSlot theWorstTimeSlot = current.getTheWorstTimeSlot();
				Solution nextSolution = trySlidingForTimeSlot(current, theWorstTimeSlot);
				current = nextSolution;
			}
		}
		return solutions;
	}

	/**
	 * tries sliding every tour in that time slot. returns the one that adds minimum
	 * "line side". Runs for infeasible solutions
	 * 
	 * @param solution
	 * @param timeSlot
	 * @return
	 */
	public static Solution trySlidingForTimeSlot(Solution solution, TimeSlot timeSlot) {
		return timeSlot.getTours().stream()
				.map(solution::slideTour)
				.sorted((s1, s2) -> s1.getTotalLineSideDif() - s2.getTotalLineSideDif())
				.findFirst().get();// fix this
	}

}
