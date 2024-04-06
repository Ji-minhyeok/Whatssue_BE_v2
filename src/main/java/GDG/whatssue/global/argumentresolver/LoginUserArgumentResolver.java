package GDG.whatssue.global.argumentresolver;

import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.global.common.annotation.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class);

        boolean assignable = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && assignable;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || ! (auth.getPrincipal() instanceof OAuth2User)) {
            log.warn("argumentresolver - @LoginUser 오류");
        }

        KakaoDetails userPrincipal = (KakaoDetails) auth.getPrincipal();

        return userPrincipal.getUser().getUserId();
    }
}
