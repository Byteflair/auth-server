package com.byteflair.oauth.server.domain;

import javax.persistence.*;

/**
 * Created by calata on 24/01/17.
 */
@Entity
@Table(name = "custom_templates")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TemplateName getName() {
        return name;
    }

    public void setName(TemplateName name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public enum TemplateName {
        LOGIN
    }
}
