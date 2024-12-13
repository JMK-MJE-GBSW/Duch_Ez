package BE.Duch_Ez.repository.group;

import BE.Duch_Ez.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {  // UUID로 변경
    boolean existsByname(String name);
    Optional<GroupEntity> findByName(String name); // 그룹 이름으로 조회
}
