package GDG.whatssue.global.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8080/swagger-ui/index.html
@Configuration
public class SpringdocConfig {
    @Bean
    public OpenAPI WhatssueOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Whatssue OpenApi")
                        .description("소모임 출석 관리 application_ Whatssue?의 Open API입니다.")
                        .version("V2")
                        .contact(new Contact()
                                .name("whatssue")
                                .email("kimphoby0125@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Whatssue V2 Github")
                        .url("https://github.com/Onion-City/Whatssue_BE_v2"));
    }

// 후에 spring security 사용할떄는  SecurityConfig 에서 뚫어주기
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/swagger-resources/**",
}