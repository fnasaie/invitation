package com.naqi.invitation.configuration;


import com.google.common.collect.Lists;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author 7cu
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public Docket productServiceApi() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(ApiInfo.DEFAULT)
                .securityContexts(Lists.newArrayList(securityContext()))
                // .securitySchemes(Lists.newArrayList(new ApiKey(HttpHeaders.COOKIE, "Cookie", "cookie")))
                .securitySchemes(Lists.newArrayList(new ApiKey("Bearer", AUTHORIZATION_HEADER, "header")))
                .useDefaultResponseMessages(false);
        docket = docket.select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
        return docket;

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("wedding-java-service")
                .description("Used to manage users, roles, authorities for clients.")
                .termsOfServiceUrl("not added yet")
                .license("not added yet")
                .licenseUrl("").version("2.1.0").build();
    }

    public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

    public SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                // new SecurityReference(HttpHeaders.COOKIE, authorizationScopes)
                // new SecurityReference(HttpHeaders.COOKIE, authorizationScopes)
                new SecurityReference("Bearer", authorizationScopes)
                );
    }
}
