package BE.Duch_Ez.repository.group;

import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.entity.group.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {

}
