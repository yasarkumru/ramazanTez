package tr.com.metu.ramazan.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import tr.com.metu.ramazan.model.Demand;

@Service
public final class DemandService {

    public static List<Demand> mergeAll(List<Demand> demands) {
        IntStream.range(0, demands.size())
                .forEach(i -> IntStream.range(i + 1, demands.size())
                        .forEach(j -> demands.get(i).merge(demands.get(j))));
        return demands.stream()
                .filter(demand -> demand.getValue() > 0)
                .collect(Collectors.toList());
    }
}
