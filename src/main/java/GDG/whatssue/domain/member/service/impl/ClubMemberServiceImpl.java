package GDG.whatssue.domain.member.service.impl;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.member.service.ClubMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public boolean isClubMember(Long clubId, Long userId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId)
            .orElse(null);

        if (clubMember == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isClubManager(Long clubId, Long userId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId).get();

        if (clubMember.getRole() == Role.MANAGER) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFirstVisit(Long clubId, Long userId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_UserId(clubId, userId).get();

        return clubMember.isFirstVisit();
    }
}
