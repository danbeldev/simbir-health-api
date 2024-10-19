package ru.simbir.health.timetableservice.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.simbir.health.timetableservice.common.security.authenticate.AuthenticateInterceptor;
import ru.simbir.health.timetableservice.common.security.user.UserSessionArgumentResolver;
import ru.simbir.health.timetableservice.features.user.client.UserServiceClient;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Lazy
    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserSessionArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createAuthenticateInterceptor())
                .addPathPatterns("/**");
    }

    private AuthenticateInterceptor createAuthenticateInterceptor() {
        return new AuthenticateInterceptor(userServiceClient);
    }
}