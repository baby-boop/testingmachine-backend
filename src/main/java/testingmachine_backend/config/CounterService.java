package testingmachine_backend.config;

import java.util.ArrayList;
import java.util.List;

public class CounterService {

    private static final List<CounterDTO> counter = new ArrayList<>();

    public static void addCounter(int count, int totalCount) {
        CounterDTO statusDTO = new CounterDTO(count, totalCount);
        counter.add(statusDTO);
    }

    public static CounterDTO getLatestCounter() {
        if (counter.isEmpty()) {
            return null;
        }
        return counter.get(counter.size() - 1);
    }
}
