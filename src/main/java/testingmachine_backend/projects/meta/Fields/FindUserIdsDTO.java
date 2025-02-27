package testingmachine_backend.projects.meta.Fields;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindUserIdsDTO {

    private String id;

    public FindUserIdsDTO(String id) {
        this.id = id;
    }
}
