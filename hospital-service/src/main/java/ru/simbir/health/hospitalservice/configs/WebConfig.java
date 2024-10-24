package ru.simbir.health.hospitalservice.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import ru.simbir.health.hospitalservice.common.security.authenticate.AuthenticateInterceptor;
import ru.simbir.health.hospitalservice.common.security.user.UserSessionArgumentResolver;
import ru.simbir.health.hospitalservice.features.user.client.UserServiceClient;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Lazy
    @Autowired
    private UserServiceClient userServiceClient;

    private final LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserSessionArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createAuthenticateInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(localeChangeInterceptor);
    }

    private AuthenticateInterceptor createAuthenticateInterceptor() {
        return new AuthenticateInterceptor(userServiceClient);
    }
}