package BE.Duch_Ez.entity.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate; // 날짜를 위한 import
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "duch_pay")
public class DuchPayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title; // 더치페이 항목 이름

    @Column(nullable = false)
    private int totalAmount; // 총 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group; // 소속 그룹

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = false)
    private ParticipantEntity payer; // 돈 낸 사람

    @OneToMany(mappedBy = "duchPay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DuchPayParticipantEntity> participants = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate paymentDate; // 날짜 추가

    public List<DuchPayParticipantEntity> getDuchPayParticipants() {
        return participants;
    }
}
