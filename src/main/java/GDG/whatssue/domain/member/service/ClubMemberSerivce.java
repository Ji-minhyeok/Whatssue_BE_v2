package GDG.whatssue.domain.member.service;

import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class ClubMemberSerivce {
    private final ClubMemberRepository clubMemberRepository;
    public void modifyClubMember(Long memberId, ClubMemberDto requestDto) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        try{
            clubMember.setMemberName(requestDto.getMemberName());
            clubMember.setMemberIntro(requestDto.getMemberIntro());
            clubMember.setEmailPublic(requestDto.isEmailPublic());
            clubMember.setPhonePublic(requestDto.isPhonePublic());
            clubMemberRepository.save(clubMember);
        }catch(Exception e){
            throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_MODIFY_ERROR);
        }
    }
}