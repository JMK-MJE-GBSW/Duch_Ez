package BE.Duch_Ez.repository.user;

import BE.Duch_Ez.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsAllByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
}
