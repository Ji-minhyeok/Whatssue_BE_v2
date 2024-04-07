package GDG.whatssue.domain.club.service;

import GDG.whatssue.domain.club.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.club.dto.GetJoinRequestsResponse;
import java.util.List;

public interface ClubJoinService {

    public void joinClub(Long userId, ClubJoinRequestDto requestDto);
    public List<GetJoinRequestsResponse> getJoinRequests(Long userId);

}
