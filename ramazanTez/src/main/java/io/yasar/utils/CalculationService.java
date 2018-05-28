package io.yasar.utils;

import java.util.List;

import org.springframework.stereotype.Service;

import io.yasar.model.Solution;
import io.yasar.model.TimeSlot;

@Service
public class CalculationService {

    public List<Solution> run(Solution solution) {
        if(solution.isFeasible()) {
            
        }else {
            TimeSlot theWorstTimeSlot = solution.getTheWorstTimeSlot();
            
        }
        return null;
    }


}
