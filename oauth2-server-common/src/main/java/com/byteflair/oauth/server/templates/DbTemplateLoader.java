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
        if (!CustomTemplate.TemplateName.exists(name)) {
            return null;
        }

        template = templateRepository
            .findOneByName(CustomTemplate.TemplateName.valueOf((removeSuffix(name).toUpperCase())));

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

    }
}
