package GDG.whatssue.domain.club.service.impl;

import GDG.whatssue.domain.club.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.club.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.ClubJoinRequest;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.club.service.ClubJoinService;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.error.CommonException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubJoinServiceImpl implements ClubJoinService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubJoinRequestRepository clubJoinRequestRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    public void joinClub(Long userId, ClubJoinRequestDto requestDto) {
        User loginUser = userRepository.findById(userId).get();
        String privateCode = requestDto.getPrivateCode();

        //가입 코드로 클럽찾기
        Club club = clubRepository.findByPrivateCode(privateCode)
            .orElseThrow(() -> new CommonException(ClubErrorCode.INVALID_PRIVATE_CODE_ERROR));

        //중복 신청 또는 이미 가입 체크
        checkJoinDuplicate(userId, club);
        checkJoinRequestDuplicate(userId, club);

        //가입 신청 만들기
        clubJoinRequestRepository.save(
            ClubJoinRequest.builder()
            .user(loginUser)
            .club(club).build());
    }

    @Override
    public List<GetJoinRequestsResponse> getJoinRequests(Long userId) {
        List<ClubJoinRequest> joinRequests = clubJoinRequestRepository.findByUser_UserId(userId);

        if (joinRequests.isEmpty()) {
            return new ArrayList<>();
        }

        List<GetJoinRequestsResponse> responseList = joinRequests.stream()
            .map(r -> GetJoinRequestsResponse.builder()
                .clubName(r.getClub().getClubName())
                .createdAt(r.getCreateAt()).build())
            .collect(Collectors.toList());

        return responseList;
    }

    private void checkJoinRequestDuplicate(Long userId, Club club) {
        //이미 가입 신청한 모임
        boolean result = clubJoinRequestRepository.findByUser_UserId(userId)
            .stream()
            .anyMatch(r -> r.getClub().equals(club));

        if (result) {
            throw new CommonException(ClubErrorCode.DUPLICATE_CLUB_JOIN_REQUEST_ERROR);
        }
    }

    private void checkJoinDuplicate(Long userId, Club club) {
        //이미 가입된 모임
        boolean result = clubMemberRepository.findByUser_UserId(userId)
            .stream()
            .anyMatch(r -> r.getClub().equals(club));

        if (result) {
            throw new CommonException(ClubErrorCode.DUPLICATE_CLUB_JOIN_ERROR);
        }
    }
}
