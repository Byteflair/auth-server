package com.byteflair.oauth.server.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by calata on 24/01/17.
 */
@Entity
@Table(name = "custom_templates")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private TemplateName name;

    @Column(name = "content", nullable = false)
    private byte[] content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified")
    private Date lastModified;

    @Column(name = "encoding", nullable = false)
    private String encoding;

    public enum TemplateName {
        LOGIN
    }
}
