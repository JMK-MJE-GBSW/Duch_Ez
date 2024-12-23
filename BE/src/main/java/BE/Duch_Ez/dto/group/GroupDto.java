package BE.Duch_Ez.dto.group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GroupDto {
    private UUID id;
    private String name;
    private List<ParticipantDto> participants;
    private Long ownerId;
    private int totalSpent; // 그룹의 총 지출 금액
}

