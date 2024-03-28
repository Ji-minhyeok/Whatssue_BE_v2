package GDG.whatssue.domain.club.repository;

import GDG.whatssue.domain.club.entity.ClubJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinRequestRepository extends JpaRepository<ClubJoinRequest, Long> {

    List<ClubJoinRequest> findByUserId(Long userId);
    List<ClubJoinRequest> findByClubId(Long clubId);
}
