package GDG.whatssue.domain.user.repository;

import GDG.whatssue.domain.user.entity.PhoneCertNum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface PhoneCertNumRepository extends CrudRepository<PhoneCertNum, String> {
    Optional<PhoneCertNum> findById(String id);
}
