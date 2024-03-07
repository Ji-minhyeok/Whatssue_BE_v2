package GDG.whatssue.domain.user.repository;

import GDG.whatssue.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    public User findByUserNick(String userNick);
    public Optional<User> findByOauth2Id(String oauth2Id);

}
