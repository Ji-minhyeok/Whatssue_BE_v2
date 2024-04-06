package GDG.whatssue.domain.member.service;

//import GDG.whatssue.domain.member.dto.ClubJoinRequestDto;
//import GDG.whatssue.domain.member.entity.ClubJoinRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
//import GDG.whatssue.domain.member.repository.ClubJoinRequestRepository;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
        import GDG.whatssue.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberManagingService {

    private final ClubMemberRepository clubMemberRepository;

    public void deleteClubMember(Long memberId) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        try{
            clubMemberRepository.delete(clubMember);
        }catch(Exception e){
            throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_DELETE_ERROR);
        }
    }

    public void modifyClubMemberRole(Long memberId, String role) {
        ClubMember clubMember = clubMemberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ClubMemberErrorCode.CLUB_MEMBER_NOT_FOUND_ERROR));
        try{
            if (role.toUpperCase().equals(Role.MANAGER.toString()))
                clubMember.setRole(Role.MANAGER);
            else if (role.toUpperCase().equals(Role.MEMBER.toString()))
                clubMember.setRole(Role.MEMBER);
            else
                throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_MODIFY_ERROR);
            clubMemberRepository.save(clubMember);
        }catch(Exception e){
            throw new CommonException(ClubMemberErrorCode.CLUB_MEMBER_COULD_NOT_MODIFY_ERROR);
        }
    }


}
