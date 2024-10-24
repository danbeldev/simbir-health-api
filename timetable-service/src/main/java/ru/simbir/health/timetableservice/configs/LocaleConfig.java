package ru.simbir.health.timetableservice.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Value("${base.locale}")
    private String defaultLocale;

    @PostConstruct
    public void init() {
        Locale.setDefault(Locale.of(defaultLocale));
        LocaleContextHolder.setLocale(Locale.of(defaultLocale));
    }

    @Bean
    public LocaleResolver localeResolver() {
        var resolver = new SessionLocaleResolver();
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

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
}