package com.byteflair.oauth.server.controllers;

import com.byteflair.oauth.server.domain.CustomTemplate;
import com.byteflair.oauth.server.service.CustomTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/custom-templates/{name}", method = RequestMethod.PUT)
    public ResponseEntity<CustomTemplate> updateCustomTemplate(@PathVariable(value = "name") String name, @RequestBody String content) {

        return new ResponseEntity(templateService.saveTemplate(name, content), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/custom-templates/{name}", method = RequestMethod.GET)
    public ResponseEntity<CustomTemplate> getCustomTemplate(@PathVariable String name) {

        return new ResponseEntity<CustomTemplate>(templateService.findTemplate(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/custom-templates/{name}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTemplate(@PathVariable String name) {

        templateService.deleteTemplate(name);

        return new ResponseEntity(HttpStatus.OK);
    }
}
