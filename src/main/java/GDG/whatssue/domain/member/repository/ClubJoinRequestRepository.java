package GDG.whatssue.domain.member.repository;

import GDG.whatssue.domain.member.entity.ClubJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinRequestRepository extends JpaRepository<ClubJoinRequest, Long> {

    List<ClubJoinRequest> findByClubId(Long clubId);
}
