package GDG.whatssue.global.interceptor;

import static GDG.whatssue.domain.member.exception.ClubMemberErrorCode.*;
import static GDG.whatssue.global.error.CommonErrorCode.*;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.service.ClubService;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.error.CommonException;
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

        Long userId = getUserId();
        Long clubId = getClubId(request);

        // 클럽 존재여부 체크
        clubService.isClubExist(clubId);

        // 클럽 멤버여부 체크
        if (!clubMemberService.isClubMember(clubId, userId)) {
            throw new CommonException(FORBIDDEN_ACCESS_ERROR);
        }

        // 클럽 관리자 체크
        ClubManager clubManager = hm.getMethodAnnotation(ClubManager.class);

        if (clubManager != null && !clubMemberService.isClubManager(clubId, userId)) {
            throw new CommonException(FORBIDDEN_ACCESS_ERROR);
        }
        
        //첫 로그인 여부 체크
        if (clubMemberService.isFirstVisit(clubId, userId)) {
            throw new CommonException(PROFILE_SETUP_REQUIRED_ERROR);
        }


        //인터셉터 통과
        return true;
    }

    private Long getClubId(HttpServletRequest request) {
        String clubId = extractPathVariableFromRequest(request, "clubId");

        try {
            return Long.parseLong(clubId);
        } catch (Exception e) {
            throw new CommonException(BAD_REQUEST);
        }
    }

    private Long getUserId() {
        KakaoDetails kaKaoDetails = getKaKaoDetails();

        try {
            return kaKaoDetails.getUser().getUserId();
        } catch (Exception e) {
            throw new CommonException(BAD_REQUEST);
        }

        return null;

    }

    private String extractPathVariableFromRequest(HttpServletRequest request, String pathVariable) {
        Map<String, String> pathVariables = (Map<String, String>) request
            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        return pathVariables.get(pathVariable);
    }
    private static KakaoDetails getKaKaoDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof OAuth2User)) {
            throw new CommonException(OAUTH_ERROR);
        }

        return (KakaoDetails) auth.getPrincipal();
    }
}
