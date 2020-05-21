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

import com.byteflair.oauth.server.templates.DbTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;

/**
 * Created by calata on 4/11/16.
 */
@Configuration
@SessionAttributes("authorizationRequest")
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Value("${api.freemarker.suffix}")
    private String freemarkerSuffix;
    @Value("${api.freemarker.prefix}")
    private String freemarkerPrefix;
    @Value("${api.freemarker.templates-path}")
    private String templatesPath;

    /**
     * Any view declared here must be defined in
     * {@link com.byteflair.oauth.server.domain.CustomTemplate.TemplateName} enum
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("logout");
    }

    @Bean
    @Qualifier(value = "freeMarkerConfigurer")
    public FreeMarkerConfigurer getFreeMarkerConfigurer() throws Exception {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        freemarker.template.Configuration configuration;
        SpringTemplateLoader springTemplateLoader;
        MultiTemplateLoader multiTemplateLoader;

        try {
            configuration = new FreeMarkerConfigurationFactory().createConfiguration();

            springTemplateLoader = new SpringTemplateLoader(
                new DefaultResourceLoader(), templatesPath);
            multiTemplateLoader = new MultiTemplateLoader(
                new TemplateLoader[] {dbTemplateLoader(), springTemplateLoader});

            configuration.setTemplateLoader(multiTemplateLoader);
            configuration.setLocalizedLookup(false);

            configurer.setConfiguration(configuration);
        } catch (IOException | TemplateException e) {
            throw new Exception("Unexpected error creating FreeMarker configuration", e);
        }

        return configurer;
    }

    @Bean
    public DbTemplateLoader dbTemplateLoader() {
        return new DbTemplateLoader();
    }

    @Bean
    @Qualifier(value = "freeMarkerViewResolver")
    public FreeMarkerViewResolver getFreeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(true);
        freeMarkerViewResolver.setSuffix(freemarkerSuffix);
        freeMarkerViewResolver.setPrefix(freemarkerPrefix);
        freeMarkerViewResolver.setOrder(1);

        return freeMarkerViewResolver;
    }

}
