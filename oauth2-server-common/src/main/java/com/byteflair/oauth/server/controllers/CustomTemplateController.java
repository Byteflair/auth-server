package com.byteflair.oauth.server.controllers;

import com.byteflair.oauth.server.boundary.CustomTemplateAssembler;
import com.byteflair.oauth.server.boundary.CustomTemplateResource;
import com.byteflair.oauth.server.domain.CustomTemplate;
import com.byteflair.oauth.server.service.CustomTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by calata on 24/01/17.
 */
@Controller
public class CustomTemplateController {

    @Autowired
    private CustomTemplateService templateService;

    @Autowired
    private CustomTemplateAssembler assembler;

    @RequestMapping(value = "/custom-templates/{name}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<CustomTemplateResource> updateCustomTemplate(@PathVariable(value = "name") String name, @RequestBody CustomTemplateResource template) {

        CustomTemplate customTemplate = null;

        Assert.hasText(name);
        Assert.hasText(template.getContent());
        Assert.hasText(template.getEncoding());

        template.setName(name);
        customTemplate = assembler.convert(template);

        return new ResponseEntity(assembler.toResource(templateService.saveTemplate(customTemplate)),
                                  HttpStatus.CREATED);
    }

    @RequestMapping(value = "/custom-templates/{name}", method = RequestMethod.GET)
    public ResponseEntity<CustomTemplateResource> getCustomTemplate(@PathVariable String name) {

        return new ResponseEntity(assembler.toResource(templateService.findTemplate(name)), HttpStatus.OK);
    }

    @RequestMapping(value = "/custom-templates/{name}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTemplate(@PathVariable String name) {

        templateService.deleteTemplate(name);

        return new ResponseEntity(HttpStatus.OK);
    }
}
