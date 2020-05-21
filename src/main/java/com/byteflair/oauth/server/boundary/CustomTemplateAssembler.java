/*
 * Copyright (c) 2020 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

        if (source == null) {
            return null;
        }

        Assert.hasText(source.getContent());
        Assert.hasText(source.getName());

        customTemplate = CustomTemplate.builder()
                                       .name(CustomTemplate.TemplateName.valueOf(source.getName().toUpperCase()))
                                       .content(Base64.decode(
                                           source.getContent().getBytes(Charset.forName(source.getEncoding()))))
                                       .encoding(source.getEncoding())
                                       .lastModified(source.getLastModified())
                                       .build();

        return customTemplate;
    }

    @Override
    public CustomTemplateResource toResource(CustomTemplate entity) {

        CustomTemplateResource resource;

        if (entity == null) {
            return null;
        }

        Assert.notNull(entity.getName());
        Assert.notNull(entity.getContent());

        resource = CustomTemplateResource.builder()
                                         .content(
                                             new String(entity.getContent(), Charset.forName(entity.getEncoding())))
                                         .name(entity.getName().toString())
                                         .encoding(entity.getEncoding())
                                         .lastModified(entity.getLastModified())
                                         .build();

        return resource;
    }
}
