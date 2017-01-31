package com.byteflair.oauth.server.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by calata on 24/01/17.
 */
@Repository
public interface CustomTemplateRepository extends CrudRepository<CustomTemplate, Integer> {

    CustomTemplate findOneByName(CustomTemplate.TemplateName name);
}
