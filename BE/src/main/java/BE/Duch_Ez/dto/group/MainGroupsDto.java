package BE.Duch_Ez.dto.group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MainGroupsDto {
    private String name;
    private List<String> participants;
}
