package BE.Duch_Ez.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DuchPayRequest {

    @NotEmpty(message = "항목 이름을 입력해주세요.")
    private String title; // 더치페이 항목 이름

    @NotNull(message = "금액을 입력해주세요.")
        private Integer totalAmount; // 총 금액

    @NotNull(message = "돈 낸 사람의 ID를 입력해주세요.")
    private UUID payerId; // 돈 낸 사람 (그룹 참가자 ID)

    @NotEmpty(message = "참여자 목록을 입력해주세요.")
    private List<UUID> participantIds; // 돈을 쓴 사람들 (그룹 참가자 ID 리스트)
}
