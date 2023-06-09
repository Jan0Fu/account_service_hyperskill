package account.repository;

import account.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByEmailIgnoreCase(String email);
}
