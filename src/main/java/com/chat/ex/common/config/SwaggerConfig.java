package com.chat.ex.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("Authorization",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.APIKEY)
//                                        .in(SecurityScheme.In.HEADER)
//                                        .bearerFormat("JWT")
//                                        .name("Authorization")
//                        )
//                )
//                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .info(new Info()
                        .title("Chat_Ex / [ 실시간 채팅 예제 명세서 ]입니다.")
                        .version("1.0.0")
                        .description(
                                """
                                WebSocket + Stomp를 이용한 실시간 채팅 예제를 작성합니다.
                                \n\n
                                JWT를 이용하여 사용자는 닉네임을 입력하고 방을 생성 후 다른 사용자를 초대하여 실시간 채팅을 진행합니다.
                                """
                        )
                );
    }

}