package BE.Duch_Ez.dto.group;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupCreateRequest {

    private String name; // 그룹 이름


    private List<String> participants; // 참가자 이름 리스트


    private Long ownerId; // 소유자 ID
}
