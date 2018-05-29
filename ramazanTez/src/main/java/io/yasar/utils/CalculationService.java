package io.yasar.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import io.yasar.model.Solution;
import io.yasar.model.TimeSlot;

@Service
public class CalculationService {

    public Set<Solution> run(Solution solution) {

        Set<Solution> solutions = new HashSet<>();
        Solution current = solution;

        while (true) {

            if (current.isFeasible()) {
                solutions.add(current);
                Solution nextSolution = trySlidingForAllTimeSlots(current);
                
                
            } else {
                TimeSlot theWorstTimeSlot = solution.getTheWorstTimeSlot();
                Solution nextSolution = trySlidingForTimeSlot(solution, theWorstTimeSlot);
                current = nextSolution;
                continue;

            }
        }
//        return null;
    }

    /**
     * tries sliding every tour in every time slot. adds the solutions that decreases tour count
     * Runs for feasible solutions
     * 
     * @param solution
     * @return
     */
    public static Solution trySlidingForAllTimeSlots(Solution solution) {
        //TODO
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
        // TODO
        int index = solution.getIndexOfTimeSlot(timeSlot);
        if (index == 0)
            return null;
        return null;
    }

}
