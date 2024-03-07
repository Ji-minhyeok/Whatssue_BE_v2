package GDG.whatssue.domain.memberJoin.service;

import GDG.whatssue.domain.member.entity.ClubJoinRequest;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.memberJoin.dto.ClubJoinRequestGetDto;
import GDG.whatssue.global.common.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberJoinService {
    private final ClubJoinRequestRepository clubJoinRequestRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public void acceptResponse(Long clubJoinRequestId) {//수락
        // 클럽 가입 요청 엔티티 조회
        ClubJoinRequest clubJoinRequest = clubJoinRequestRepository.findById(clubJoinRequestId)
                .orElseThrow(() -> new IllegalArgumentException("클럽 가입 요청을 찾을 수 없습니다: " + clubJoinRequestId));

        // 클럽 멤버 엔티티 생성
        ClubMember clubMember = ClubMember.builder()
                .club(clubJoinRequest.getClub())
                .user(clubJoinRequest.getUser())
                .role(Role.MEMBER) // 초기는 MEMBER 롤 설정
                .build();

        clubMemberRepository.save(clubMember);
        clubJoinRequestRepository.delete(clubJoinRequest);
    }

    @Transactional
    public void denyResponse(Long clubJoinRequestId) {//거절
        // 클럽 가입 요청 엔티티 조회
        ClubJoinRequest clubJoinRequest = clubJoinRequestRepository.findById(clubJoinRequestId)
                .orElseThrow(() -> new IllegalArgumentException("클럽 가입 요청을 찾을 수 없습니다: " + clubJoinRequestId));

        clubJoinRequestRepository.delete(clubJoinRequest);
    }

    @Transactional
    public List<ClubJoinRequestGetDto> getClubJoinRequests(Long clubId) {
        List<ClubJoinRequest> clubJoinRequests = clubJoinRequestRepository.findByClubId(clubId);

        // Dto 매핑
        List<ClubJoinRequestGetDto> dtos = clubJoinRequests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return dtos;
    }

    // Dto 매핑
    private ClubJoinRequestGetDto mapToDto(ClubJoinRequest clubJoinRequest) {
        return ClubJoinRequestGetDto.builder()
                .id(clubJoinRequest.getId())
                .club(clubJoinRequest.getClub())
                .user(clubJoinRequest.getUser())
                .build();
    }

    @Transactional
    public void deleteMember(Long clubId, Long userId) {//멤버 삭제
        // 해당 클럽 ID와 사용자 ID에 해당하는 클럽 멤버를 조회합니다.
        Optional<ClubMember> clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId);
            clubMemberRepository.delete(clubMember.get());
    }
}