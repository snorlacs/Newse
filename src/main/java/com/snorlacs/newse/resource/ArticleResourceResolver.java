package com.snorlacs.newse.resource;

import com.snorlacs.newse.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ArticleResourceResolver extends ResourceResolver<Article, ArticleResource> {

    @Autowired
    private EntityLinks entityLinks;

    public static final String UPDATE_REL = "update";
    public static final String DELETE_REL = "delete";

    @Override
    public ArticleResource toResource(Article article) {

        ArticleResource articleResource = new ArticleResource(article);

        final Link selfLink = entityLinks.linkToSingleResource(article);

        articleResource.add(selfLink.withSelfRel());
        articleResource.add(selfLink.withRel(UPDATE_REL));
        articleResource.add(selfLink.withRel(DELETE_REL));

        return articleResource;
    }
}
