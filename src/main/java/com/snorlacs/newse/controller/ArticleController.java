package com.snorlacs.newse.controller;

import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.repository.ArticleRepository;
import com.snorlacs.newse.resource.ArticleResource;
import com.snorlacs.newse.resource.ArticleResourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ExposesResourceFor(Article.class)
@RequestMapping(value = "/article", produces = "application/json")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleResourceResolver articleResourceResolver;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<ArticleResource> createArticle(@RequestBody Article article) {
        Article createdArticle = articleRepository.create(article);
        return new ResponseEntity<>(articleResourceResolver.toResource(createdArticle), HttpStatus.CREATED);
    }
}
