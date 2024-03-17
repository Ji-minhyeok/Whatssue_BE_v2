package GDG.whatssue.domain.user.service;

import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.user.dto.SignUpRequestDto;
import GDG.whatssue.domain.user.dto.UserDto;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.global.common.Role;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Getter
public class CustomOauth2Service extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    /*
    Third party 접근을 위한 accessToken 발급 이후 실행됨
     */

    private User findOrSaveUser(OAuth2User oAuth2User, String registrationId, String name) {
        String oauth2Id = registrationId + ":" + oAuth2User.getName(); // name = ID값
        // 임시 유저로 역할 설정
//        Role role = roleRepository.findByName(RoleName.TEMPORARY)
//                .orElseThrow(() -> new OAuth2AuthenticationException("존재하지 않는 권한입니다."));

        return userRepository.findByOauth2Id(oauth2Id)
                .orElseGet(() -> userRepository.save(User.builder()
                        .oauth2Id(oauth2Id)
                        .userName(name)
                        .clubMemberList(new ArrayList<>())
                        .clubJoinRequestList(new ArrayList<>())
                        .role(String.valueOf(Role.MEMBER))
                        .build()));
    }

    @Override
    public KakaoDetails loadUser(OAuth2UserRequest userRequest) {
        // accessToken으로 서드파티에 요청해서 사용자 정보를 얻어옴

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        User user = findOrSaveUser(oAuth2User, userRequest.getClientRegistration().getRegistrationId(), profile.get("nickname").toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<ClubMember> clubMemberList = user.getClubMemberList();
        for (ClubMember clubMember : clubMemberList) {
            authorities.add((GrantedAuthority) () -> {
                Long clubId = clubMember.getClub().getId();
                Role role = clubMember.getRole();
                System.out.println("ROLE_" + clubId + role);
                return "ROLE_" + clubId + role;
            });
        }
        KakaoDetails kakaoDetails = KakaoDetails.builder()
                .registrationId(userRequest.getClientRegistration().getRegistrationId())
                .user(user)
                .authorities(authorities)
                .attributes(oAuth2User.getAttributes())
                .build();
        return kakaoDetails;
        // oauthPrincipal이 return이 되면 시큐리티 session의 Authentication의 내부에  저장이 된다.
        // Session(내부 Authentication(내부 oauthUserDetails))
    }

    public UserDto getUserInfo(KakaoDetails kakaoDetails) {
        User user = kakaoDetails.getUser();
        Long userId = user.getUserId();
        user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
         // user정보를 Repository에서 가져와야 signup 이후의 정보(phone, name)를 가져올 수 있음.
        UserDto dto = UserDto.builder()
                .userId(String.valueOf(user.getUserId()))
                .userName(user.getUserName())
                .role(user.getRole())
                .oauth2Id(user.getOauth2Id())
                .userPhone(user.getUserPhone())
                .build();
        return dto;
    }
    public UserDto signUp(KakaoDetails kakaoDetails, SignUpRequestDto request){
        User user = kakaoDetails.getUser();
        user.setUserPhone(request.getUserPhone());
        user.setUserName(request.getUserName());
        userRepository.save(user);
        return UserDto.builder()
                .userId(String.valueOf(user.getUserId()))
                .userName(request.getUserName())
                .role(user.getRole())
                .oauth2Id(user.getOauth2Id())
                .userPhone(request.getUserPhone())
                .build();
    }
}