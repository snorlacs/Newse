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

import java.util.Optional;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ArticleResource> findOrderById(@PathVariable Long id) {
        Optional<Article> article = articleRepository.findById(id);

        return article
                .map(article1 -> new ResponseEntity<>(articleResourceResolver.toResource(article1), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteArticleById(@PathVariable Long id) {
        boolean isDeleted = articleRepository.delete(id);
        HttpStatus httpStatus = isDeleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(httpStatus);
    }


}
