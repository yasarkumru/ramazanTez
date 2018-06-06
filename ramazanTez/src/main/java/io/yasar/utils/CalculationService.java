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
			Optional<Solution> nextSolution = null;
			if (current.isFeasible()) {
				solutions.add(current);
				nextSolution = trySlidingFractionalTours(current);
			} else {// if not feasible
				final TimeSlot theWorstTimeSlot = current.getTheWorstTimeSlot();
				nextSolution = trySlidingForTimeSlot(current,
						theWorstTimeSlot);
			}
			if (!nextSolution.isPresent())
				break;
			Solution nextSolutionObj = nextSolution.get();
			if (current.isSamePerf(nextSolutionObj))
				break;
			current = nextSolutionObj;
		}
		return solutions;
	}

	/**
	 * tries sliding every fractional tour in the solution. returns the one that adds
	 * minimum "tour" Runs for feasible solutions
	 * 
	 * @param current
	 * @return
	 */
	private static Optional<Solution> trySlidingFractionalTours(Solution current) {
		return current.getTimeSlots()
				.stream()
				.skip(2)
				.flatMap(ts -> ts.getTours().stream())
				.filter(Tour::isFractional)
				.map(current::slideTour)
				.sorted((s1, s2) -> s1.getPriorityHeuristic() - s2.getPriorityHeuristic())
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
				.sorted((s1, s2) -> s1.getTotalLineSideDif() == s2.getTotalLineSideDif()
						? s1.getTotalTourCount() - s2.getTotalTourCount()
						: s1.getTotalLineSideDif() - s2.getTotalLineSideDif())
				.findFirst();
	}

}
