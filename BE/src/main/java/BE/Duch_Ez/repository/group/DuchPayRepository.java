package BE.Duch_Ez.repository.group;

import BE.Duch_Ez.entity.group.DuchPayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DuchPayRepository extends JpaRepository<DuchPayEntity, UUID> {

    // 특정 그룹에 속한 더치페이 항목 조회
    List<DuchPayEntity> findByGroup_Id(UUID groupId);
}
