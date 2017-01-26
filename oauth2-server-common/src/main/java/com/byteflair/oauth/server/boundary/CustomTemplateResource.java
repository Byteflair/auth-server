package com.byteflair.oauth.server.boundary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Created by calata on 26/01/17.
 */
@Getter
@Setter
@Builder
public class CustomTemplateResource extends ResourceSupport {

    private String name;
    private String content;
    private Date last_modified;
    private String encoding;

    @JsonCreator
    public CustomTemplateResource(@JsonProperty("name") String name,
                                  @JsonProperty("content") String content, @JsonProperty("last_modified") Date last_modified,
                                  @JsonProperty("encoding") String encoding) {
        this.name = name;
        this.content = content;
        this.last_modified = last_modified;
        this.encoding = encoding;
    }
}
