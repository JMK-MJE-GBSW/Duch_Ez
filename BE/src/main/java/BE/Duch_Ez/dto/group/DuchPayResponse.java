package BE.Duch_Ez.dto.group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DuchPayResponse {

    private UUID id; // 더치페이 항목 ID
    private String title; // 항목 이름
    private int totalAmount; // 총 금액
    private UUID payerId; // 돈 낸 사람 ID
    private List<ParticipantDebt> participantDebts; // 각 참여자의 지불 내역

    @Getter
    @Setter
    public static class ParticipantDebt {
        private UUID participantId;
        private String participantName;
        private int amountOwed; // 지불해야 할 금액
    }
}
