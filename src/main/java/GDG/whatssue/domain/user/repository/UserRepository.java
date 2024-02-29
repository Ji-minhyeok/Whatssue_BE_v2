package GDG.whatssue.domain.user.repository;

import GDG.whatssue.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUserNick(String userNick);


}