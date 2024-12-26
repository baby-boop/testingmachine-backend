package testingmachine_backend.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounterDTO {

    private int count;
    private int totalCount;

    public CounterDTO( int count, int totalCount ) {
        this.count = count;
        this.totalCount = totalCount;
    }
}
