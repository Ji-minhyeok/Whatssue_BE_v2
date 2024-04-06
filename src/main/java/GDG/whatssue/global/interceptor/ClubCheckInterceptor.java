package GDG.whatssue.global.interceptor;

import GDG.whatssue.domain.club.service.ClubService;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.global.annotation.ClubManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
@Component
public class ClubCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod hm = (HandlerMethod) handler;

        Long userId = extractUserIdFromKakao();
        Long clubId = extractClubIdFromRequest(request);

        if (userId == null) {
            log.warn("Kakao Oauth에서 userId 추출 오류");
            return false;
        }

        if (clubId == null) {
            log.warn("request에서 clubId 추출 오류");
            return false;
        }

        log.info("userId: " + userId);

        // 클럽 존재여부 체크 TODO
        if (!clubService.isClubExist(clubId)) {
            //처리 TODO
            log.info("존재하지 않는 클럽입니다");
            return false;
        }

        // 클럽 멤버여부 체크 TODO
        if (!clubMemberService.isClubMember(clubId, userId)) {
            //처리 TODO
            log.info("멤버가 아닙니다");
            return false;
        }

        // 클럽 관리자 체크 TODO
        ClubManager clubManager = hm.getMethodAnnotation(ClubManager.class);

        if (clubManager == null) { //관리자 api가 아니면 true
            return true;
        }

        if (!clubMemberService.isClubManager(clubId, userId)) {
            log.info("관리자가 아닙니다");
            return false;
        }

        return true;
    }


    //동작하는지 체크 TODO
    private static Long extractUserIdFromKakao() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null || auth.getPrincipal() instanceof OAuth2User) {
            KakaoDetails userPrincipal = (KakaoDetails) auth.getPrincipal();

            return userPrincipal.getUser().getUserId();
        }
        return null;
    }

    private static Long extractClubIdFromRequest(HttpServletRequest request) {
        Map<String, String> pathVariables = (Map<String, String>) request
            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String clubId = pathVariables.get("clubId");

        if (clubId == null) {
            return null;
        } else {
            return Long.parseLong(clubId);
        }
    }
}
