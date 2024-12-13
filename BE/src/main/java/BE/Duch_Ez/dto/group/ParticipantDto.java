package BE.Duch_Ez.dto.group;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParticipantDto {

    private UUID id;  // UUID 타입으로 변경
    private String name;

    private UUID groupId;  // UUID로 변경
    private String groupName;
}
