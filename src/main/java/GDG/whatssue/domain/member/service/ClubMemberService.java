package GDG.whatssue.domain.member.service;

public interface ClubMemberService {

    public boolean isClubMember(Long clubId, Long userId);
    public boolean isClubManager(Long clubId, Long userId);

}
