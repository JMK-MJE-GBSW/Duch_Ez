package BE.Duch_Ez.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate; // 날짜를 위한 import
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DuchPayRequest {

    @NotEmpty(message = "항목 이름을 입력해주세요.")
    private String title;

    @NotNull(message = "금액을 입력해주세요.")
    private Integer totalAmount;

    @NotNull(message = "돈 낸 사람의 ID를 입력해주세요.")
    private UUID payerId;

    @NotEmpty(message = "참여자 목록을 입력해주세요.")
    private List<UUID> participantIds;

    @NotNull(message = "결제 날짜를 입력해주세요.") // 유효성 검사
    private LocalDate paymentDate; // 날짜 추가
}
