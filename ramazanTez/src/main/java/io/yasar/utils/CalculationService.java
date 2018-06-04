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
				solutions.add(current);
				Optional<Solution> nextSolution = tryFeasible(current);
				if (!nextSolution.isPresent())
					break;
				current = nextSolution.get();
			} else {// if not feasible
				current = trySlidingForTimeSlot(current, current.getTheWorstTimeSlot()).get();
			}
		}
		return solutions;
	}

	/**
	 * tries sliding every fractional tour in the solution. returns the one that adds minimum
	 * "tour" Runs for feasible solutions
	 * 
	 * @param current
	 * @return
	 */
	private static Optional<Solution> tryFeasible(Solution current) {
		return current.getTimeSlots()
				.stream()
				.skip(1)
				.flatMap(ts -> ts.getTours().stream())
				.filter(Tour::isFractional)
				.map(current::slideTour)
				.sorted((s1, s2) -> {
					if(s1.getTotalTourCount() == s2.getTotalTourCount())
						return s1.getTotalLineSideDif() - s2.getTotalLineSideDif();
					return s1.getTotalTourCount() - s2.getTotalTourCount();
				})
				.findFirst();
	}

	/**
	 * tries sliding every tour in that time slot. returns the one that adds minimum
	 * "line side". Runs for infeasible solutions
	 * 
	 * @param solution
	 * @param timeSlot
	 * @return
	 */
	private static Optional<Solution> trySlidingForTimeSlot(Solution solution, TimeSlot timeSlot) {
		return timeSlot.getTours().stream()
				.map(solution::slideTour)
				.sorted((s1, s2) -> s1.getTotalLineSideDif() - s2.getTotalLineSideDif())
				.findFirst();
	}

}
