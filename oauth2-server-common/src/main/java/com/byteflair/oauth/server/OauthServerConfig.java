package com.byteflair.oauth.server;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PreDestroy;

/**
 * Created by calata on 15/11/16.
 */
@Configuration
@Import(value = {
    OAuth2AuthorizationConfig.class,
    SecurityConfig.class,
    MvcConfig.class
})
@ComponentScan
@EnableAutoConfiguration
@SpringBootConfiguration
public class OauthServerConfig {

    @PreDestroy
    public void onShutdown() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.stop();
    }
}
