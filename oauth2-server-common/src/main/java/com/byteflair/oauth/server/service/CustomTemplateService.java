package com.byteflair.oauth.server.service;

import com.byteflair.oauth.server.domain.CustomTemplate;
import com.byteflair.oauth.server.domain.CustomTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.util.Date;

/**
 * Created by calata on 24/01/17.
 */
@Service
public class CustomTemplateService {

    @Autowired
    private CustomTemplateRepository templateRepository;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    public CustomTemplate saveTemplate(CustomTemplate template) {
        CustomTemplate oldTemplate;

        oldTemplate = findTemplate(template.getName().toString()); // si existe, lo recuperamos
        if (oldTemplate != null) {
            template.setId(oldTemplate.getId());
        }

        template.setLastModified(new Date());
        template = templateRepository.save(template);
        freeMarkerConfig.getConfiguration().clearTemplateCache();

        return template;

    }

    public CustomTemplate findTemplate(String name) {

        Assert.hasText(name);

        return templateRepository.findOneByName(CustomTemplate.TemplateName.valueOf(name.toUpperCase()));
    }

    public void deleteTemplate(String name) {

        CustomTemplate template;
        Assert.hasText(name);

        template = findTemplate(name);
        if (template != null) {
            templateRepository.delete(template);
        }

        freeMarkerConfig.getConfiguration().clearTemplateCache();
    }
}
