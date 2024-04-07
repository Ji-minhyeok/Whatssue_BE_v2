package GDG.whatssue.global.config;

import GDG.whatssue.global.argumentresolver.LoginUserArgumentResolver;
import GDG.whatssue.global.interceptor.ClubCheckInterceptor;
import GDG.whatssue.global.interceptor.ScheduleCheckInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ClubCheckInterceptor clubCheckInterceptor;
    private final ScheduleCheckInterceptor scheduleCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clubCheckInterceptor)
            .order(1)
            .addPathPatterns("/api/clubs/{clubId}/**");

        registry.addInterceptor(scheduleCheckInterceptor)
            .order(2)
            .addPathPatterns("/api/clubs/{clubId}/schedules/{scheduleId}/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}
