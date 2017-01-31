package com.byteflair.oauth.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * Created by calata on 17/01/17.
 */
@Configuration
@EnableResourceServer
public class Oauth2ResourceServer extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.requestMatcher(new OrRequestMatcher(
            new AntPathRequestMatcher("/clients/**"),
            new AntPathRequestMatcher("/users/**"),
            new AntPathRequestMatcher("/user-states/**"),
            new AntPathRequestMatcher("/user-details/**"),
            new AntPathRequestMatcher("/roles/**"),
            new AntPathRequestMatcher("/groups/**"),
            new AntPathRequestMatcher("/profile/**"),
            new AntPathRequestMatcher("/custom-templates/**"),
            new AntPathRequestMatcher("/alps/**"),
            new AntPathRequestMatcher("/css/**"),
            new AntPathRequestMatcher("/images/**")
        ))
            .authorizeRequests()
            .antMatchers("/clients/**",
                         "/users/**",
                         "/user-states/**",
                         "/user-details/**",
                         "/roles/**",
                         "/groups/**",
                         "/alps/**",
                         "/profile/**",
                         "/custom-templates/**").hasRole("ADMIN")
            .antMatchers("/css/**", "/images/**").permitAll();

    }
}
