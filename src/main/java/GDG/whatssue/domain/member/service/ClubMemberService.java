package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.member.entity.ClubJoinRequest;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubJoinRequestRepository clubJoinRequestRepository;

    public void addClubJoinRequest(Long userId, String clubCode) {
        Club club = clubRepository.findByClubCode(clubCode)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_NOT_FOUND_ERROR));

        User loginUser = userRepository.findById(userId).get();

        checkJoinDuplicate(loginUser, club);

        ClubJoinRequest newClubJoinRequest = ClubJoinRequest.builder()
            .club(club).user(loginUser).build();

        clubJoinRequestRepository.save(newClubJoinRequest);
    }

    private void checkJoinDuplicate(User loginUser, Club club) {
        boolean result = clubJoinRequestRepository.findAll()
            .stream()
            .anyMatch(r -> r.getClub().equals(club) && r.getUser().equals(loginUser));

        if (result) {
            throw new CommonException(ClubMemberErrorCode.DUPLICATE_CLUB_JOIN_ERROR);
        }
    }
}
