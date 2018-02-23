package com.snorlacs.newse.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

@JsonAutoDetect
public class Article implements Identifiable {

    private Long id;
    private String header;
    private String shortDescription;
    private String text;
    private Date publishedOn;
    private List<Author> authors;
    private List<String> keywords;


    @Override
    public Long getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getText() {
        return text;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getPublishedOn() {
        return publishedOn;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
