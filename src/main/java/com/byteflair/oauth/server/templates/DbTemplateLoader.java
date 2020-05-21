/*
 * Copyright (c) 2020 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.byteflair.oauth.server.templates;

import com.byteflair.oauth.server.domain.CustomTemplate;
import com.byteflair.oauth.server.domain.CustomTemplateRepository;
import freemarker.cache.TemplateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by calata on 25/01/17.
 */
public class DbTemplateLoader implements TemplateLoader {

    @Autowired
    CustomTemplateRepository templateRepository;
    @Value("${api.freemarker.suffix}")
    private String freemarkerSuffix;

    @Override
    public Object findTemplateSource(String name) throws IOException {

        CustomTemplate template;

        // check if custom-template is defined in enum
        if (!CustomTemplate.TemplateName.exists(removeSuffix(name))) {
            return null;
        }

        template = templateRepository
            .findOneByName(CustomTemplate.TemplateName.valueOf(removeSuffix(name).toUpperCase()));

        if (template != null) {
            return template;
        }

        return null;
    }

    private String removeSuffix(String name) {
        if (name.contains(freemarkerSuffix)) { // remove suffix
            return name.substring(0, name.indexOf(freemarkerSuffix));
        }
        return name;
    }

    @Override
    public long getLastModified(Object templateSource) {
        if (templateSource != null && templateSource.getClass().isAssignableFrom(CustomTemplate.class)) {
            return ((CustomTemplate) templateSource).getLastModified().getTime();
        }

        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return new InputStreamReader(new ByteArrayInputStream(((CustomTemplate) templateSource).getContent()),
                                     encoding);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        // Do nothing
    }
}
