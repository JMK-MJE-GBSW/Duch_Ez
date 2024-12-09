package BE.Duch_Ez.dto.group;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupDto {

    @NotEmpty(message = "그룹 이름이 비어있습니다")
    private String name;

    private List<String> participants;

    private Long ownerId;
}
