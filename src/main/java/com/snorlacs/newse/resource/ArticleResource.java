package com.snorlacs.newse.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.domain.Author;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;

public class ArticleResource extends ResourceSupport {

    private final long id;
    private final String header;
    private final String shortDescription;
    private final String text;
    private final Date publishedOn;
    private final List<Author> authors;
    private final List<String> keywords;

    public ArticleResource(Article article) {
        this.id = article.getId();
        this.header = article.getHeader();
        this.shortDescription = article.getShortDescription();
        this.text = article.getText();
        this.publishedOn = article.getPublishedOn();
        this.authors = article.getAuthors();
        this.keywords = article.getKeywords();
    }

    @JsonProperty("id")
    public Long getResourceId() {
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

    public Date getPublishedOn() {
        return publishedOn;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
