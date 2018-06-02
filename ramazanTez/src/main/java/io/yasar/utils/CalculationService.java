package io.yasar.utils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.yasar.model.Solution;
import io.yasar.model.TimeSlot;
import io.yasar.model.Tour;

@Service
public class CalculationService {

    public Set<Solution> run(Solution solution) {

        Set<Solution> solutions = new HashSet<>();
        Solution current = solution;

        while (true) {
        	System.out.println(current);
            if (current.isFeasible()) {
                if (current.isFinished())
                    break;
                Optional<Solution> nextSolution = current.getTimeSlots()
                        .stream()
                        .flatMap(ts -> ts.getTours().stream())
                        .filter(Tour::isFractional)
                        .map(current::slideTour)
                        .peek(solutions::add)
                        .sorted()
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
     * tries sliding every tour in every time slot. adds the solutions that decreases tour count
     * Runs for feasible solutions
     * 
     * @param solution
     * @return
     */
    public static Solution trySlidingForAllTimeSlots(Solution solution) {
        // TODO

        return null;
    }

    /**
     * tries sliding every tour in that time slot. returns the one that adds minimum "line side".
     * Runs for infeasible solutions
     * 
     * @param solution
     * @param timeSlot
     * @return
     */
    public static Solution trySlidingForTimeSlot(Solution solution, TimeSlot timeSlot) {
        return timeSlot.getTours().stream()
                .map(solution::slideTour)
                .sorted((s1, s2) -> s1.getTotalLineSideDif() - s2.getTotalLineSideDif())
                .collect(Collectors.toList()).get(0);//fix this
    }

}
