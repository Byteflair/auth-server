/*
 * Copyright (c) 2020 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
