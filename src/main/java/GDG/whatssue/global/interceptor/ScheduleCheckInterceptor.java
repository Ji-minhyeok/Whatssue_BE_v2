package GDG.whatssue.global.interceptor;

import static GDG.whatssue.global.error.CommonErrorCode.*;

import GDG.whatssue.domain.schedule.service.impl.ScheduleServiceImpl;
import GDG.whatssue.global.error.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class ScheduleCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Long clubId = getClubId(request);
        Long scheduleId = getScheduleId(request);

        //클럽의 스케줄 체크
        if (!scheduleService.isClubSchedule(clubId, scheduleId)) {
            throw new CommonException(FORBIDDEN_ACCESS_ERROR);
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

    private Long getScheduleId(HttpServletRequest request) {
        String scheduleId = extractPathVariableFromRequest(request, "scheduleId");

        try {
            return Long.parseLong(scheduleId);
        } catch (Exception e) {
            throw new CommonException(BAD_REQUEST);
        }
    }

    private String extractPathVariableFromRequest(HttpServletRequest request, String pathVariable) {
        Map<String, String> pathVariables = (Map<String, String>) request
            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        return pathVariables.get(pathVariable);
    }
}
