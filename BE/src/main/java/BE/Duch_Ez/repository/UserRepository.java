package BE.Duch_Ez.repository;

import BE.Duch_Ez.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsAllByEmail(String email);
}
