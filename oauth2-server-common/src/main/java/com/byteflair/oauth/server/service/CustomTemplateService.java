package com.byteflair.oauth.server.service;

import com.byteflair.oauth.server.domain.CustomTemplate;
import com.byteflair.oauth.server.domain.CustomTemplateRepository;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;

/**
 * Created by calata on 24/01/17.
 */
@Service
public class CustomTemplateService {

    @Autowired
    private CustomTemplateRepository templateRepository;

    public CustomTemplate saveTemplate(String name, String content64) {

        byte[] decodedContent;
        CustomTemplate template = null;

        Assert.hasText(name);
        Assert.hasText(content64);

        try {

            template = findTemplate(name); // si existe, lo recuperamos
            if (template == null) {
                template = new CustomTemplate();
            }
            // convert base64 string to byte[]
            decodedContent = Base64.decode(content64.getBytes("UTF-8"));

            template.setContent(decodedContent);
            template.setName(CustomTemplate.TemplateName.valueOf(name.toUpperCase()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // TODO
        }

        return templateRepository.save(template);
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
    }
}
