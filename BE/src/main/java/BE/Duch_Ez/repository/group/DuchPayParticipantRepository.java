package BE.Duch_Ez.repository.group;

import BE.Duch_Ez.entity.group.DuchPayParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DuchPayParticipantRepository extends JpaRepository<DuchPayParticipantEntity, UUID> {

    // 특정 더치페이 항목에 참여한 사람들 조회
    List<DuchPayParticipantEntity> findByDuchPay_Id(UUID dutchPayId);
}
