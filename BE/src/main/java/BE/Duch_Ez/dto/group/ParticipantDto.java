package BE.Duch_Ez.dto.group;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ParticipantDto {

    @NotEmpty(message = "그룹 참가자 id가 비어있습니다")
    private Long id;

    @NotEmpty(message = "그룹 참가자 name이 비어있습니다")
    private String name;
}
