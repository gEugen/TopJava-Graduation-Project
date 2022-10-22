package ru.javaops.topjava.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Graduation work on <a href='https://javaops.ru/view/topjava'>Java Enterprise Online Project</a>
                        <p><b>Test credentials:</b><br>
                        - Admin: admin@gmail.com / admin<br>
                        - User1:  user1@yandex.ru / password1<br>
                        - User2:  user2@yandex.ru / password2<br>
                        - User3:  user3@yandex.ru / password3<br>
                        - User4:  user4@yandex.ru / password4<br>
                        - User5:  user5@yandex.ru / password5<br>
                        - User6:  user6@yandex.ru / password6<br>
                        - Guest: guest@gmail.com / guest</p>
                        """,
                contact = @Contact(name = "Evgeny Prokofiev", email = "eug.tackleberry@gmail.com")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}
