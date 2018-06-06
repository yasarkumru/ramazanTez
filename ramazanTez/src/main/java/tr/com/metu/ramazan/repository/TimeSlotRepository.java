package tr.com.metu.ramazan.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import tr.com.metu.ramazan.excel.Constants;
import tr.com.metu.ramazan.model.TimeSlot;

@Component
public class TimeSlotRepository {

	private List<TimeSlot> timeSlots;

	@PostConstruct
	public void init() {
		// skip first timeSlot
		timeSlots = IntStream.range(0, Constants.TIME_SLOT_COUNT)
				.mapToObj(TimeSlot::new)
				.collect(Collectors.toList());
	}

	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}

	public TimeSlot findTimeSlotByRank(Integer rank) {
		return timeSlots.stream()
				.filter(timeSlot -> timeSlot.getRank().equals(rank))
				.findFirst().get();
	}

}
