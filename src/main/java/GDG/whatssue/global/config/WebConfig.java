package GDG.whatssue.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

/*    private final ScheduleInterceptor scheduleInterceptor;

    *//**
     * PathMatcher를 따로 구현해서 HttpMethod 별로 excludePatterns 구현하기 TODO
     *//*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("add ScheduleInterceptor");
        registry.addInterceptor(scheduleInterceptor)
            .order(1)
            .addPathPatterns("/api/{clubId}/schedules/{scheduleId}");
    }

    @PostConstruct
    void init() {
        log.info("Web Config init");
    }*/
}
