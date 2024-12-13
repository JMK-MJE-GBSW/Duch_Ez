package BE.Duch_Ez.entity.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "duch_pay_participants")
public class DuchPayParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duch_pay_id", nullable = false)
    private DuchPayEntity duchPay; // 연결된 더치페이 항목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private ParticipantEntity participant; // 참여자

    @Column(nullable = false)
    private int amountOwed; // 이 사람이 지불해야 할 금액
}
