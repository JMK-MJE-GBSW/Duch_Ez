package BE.Duch_Ez.repository.group;

import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {
    boolean existsByNameAndOwner(String name, UserEntity owner); // 사용자별 그룹 이름 중복 검사
    Optional<GroupEntity> findByName(String name);
    List<GroupEntity> findByOwner(UserEntity owner);
}
