package ru.simbir.health.accountservice.configs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver {

    @Value("${base.locale}")
    private String defaultLocale;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        return acceptLanguage == null || acceptLanguage.isEmpty()
                ? Locale.of(defaultLocale)
                : Locale.forLanguageTag(acceptLanguage);
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.of(defaultLocale));
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}