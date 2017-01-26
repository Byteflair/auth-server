package com.byteflair.oauth.server.boundary;

import com.byteflair.oauth.server.controllers.CustomTemplateController;
import com.byteflair.oauth.server.domain.CustomTemplate;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.core.convert.converter.Converter;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * Created by calata on 26/01/17.
 */
@Component
public class CustomTemplateAssembler extends ResourceAssemblerSupport<CustomTemplate, CustomTemplateResource> implements Converter<CustomTemplateResource, CustomTemplate> {

    public CustomTemplateAssembler() {
        super(CustomTemplateController.class, CustomTemplateResource.class);
    }

    @Override
    public CustomTemplate convert(CustomTemplateResource source) {

        CustomTemplate customTemplate;

        Assert.notNull(source);
        Assert.hasText(source.getContent());
        Assert.hasText(source.getName());

        customTemplate = CustomTemplate.builder()
                                       .name(CustomTemplate.TemplateName.valueOf(source.getName().toUpperCase()))
                                       .content(Base64.decode(
                                           source.getContent().getBytes(Charset.forName(source.getEncoding()))))
                                       .encoding(source.getEncoding())
                                       .lastModified(source.getLast_modified())
                                       .build();

        return customTemplate;
    }

    @Override
    public CustomTemplateResource toResource(CustomTemplate entity) {

        CustomTemplateResource resource;
        Assert.notNull(entity);
        Assert.notNull(entity.getName());
        Assert.notNull(entity.getContent());

        resource = CustomTemplateResource.builder()
                                         .content(
                                             new String(entity.getContent(), Charset.forName(entity.getEncoding())))
                                         .name(entity.getName().toString())
                                         .encoding(entity.getEncoding())
                                         .last_modified(entity.getLastModified())
                                         .build();

        return resource;
    }
}
