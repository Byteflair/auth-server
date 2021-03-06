/*
 * Copyright (c) 2020 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
        CustomTemplate updated;

        oldTemplate = findTemplate(template.getName().toString()); // si existe, lo recuperamos
        if (oldTemplate != null) {
            template.setId(oldTemplate.getId());
        }

        template.setLastModified(new Date());
        updated = templateRepository.save(template);
        freeMarkerConfig.getConfiguration().clearTemplateCache();

        return updated;

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
