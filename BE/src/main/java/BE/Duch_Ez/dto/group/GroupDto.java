package BE.Duch_Ez.dto.group;

import BE.Duch_Ez.entity.group.ParticipantEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupDto {

    private Long id;
    @NotEmpty(message = "그룹 이름이 비어있습니다")
    private String name;

    private List<ParticipantDto> participants;

    private Long ownerId;

}
