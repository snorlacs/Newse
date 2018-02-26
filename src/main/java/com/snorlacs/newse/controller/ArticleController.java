package com.snorlacs.newse.controller;

import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.repository.ArticleRepository;
import com.snorlacs.newse.resource.ArticleResource;
import com.snorlacs.newse.resource.ArticleResourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.snorlacs.newse.domain.JsonDateSerializer.DATE_FORMAT;

@RestController
@ExposesResourceFor(Article.class)
@RequestMapping(produces = "application/json")
public class ArticleController {

    private final ArticleRepository articleRepository;

    private final ArticleResourceResolver articleResourceResolver;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, ArticleResourceResolver articleResourceResolver) {
        this.articleRepository = articleRepository;
        this.articleResourceResolver = articleResourceResolver;
    }

    @RequestMapping(value = "/article", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<ArticleResource> createArticle(@RequestBody @Valid Article article) {
        Article createdArticle = articleRepository.create(article);
        return new ResponseEntity<>(articleResourceResolver.toResource(createdArticle), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public ResponseEntity<ArticleResource> findArticleById(@PathVariable Long id) {
        Optional<Article> article = articleRepository.findOne(id);

        return article
                .map(article1 -> new ResponseEntity<>(articleResourceResolver.toResource(article1), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        boolean isDeleted = articleRepository.delete(id);
        HttpStatus httpStatus = isDeleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<ArticleResource> updateArticle(@PathVariable Long id, @RequestBody Article updatedArticle) {
        boolean updated = articleRepository.update(id, updatedArticle);
        if (updated) {
            return findArticleById(id);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public ResponseEntity<Collection<ArticleResource>> filterArticles(@RequestParam(value = "keyword", required = false) String keyword,
                                                                      @RequestParam(value = "author", required = false) String author,
                                                                      @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = DATE_FORMAT) Date from,
                                                                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = DATE_FORMAT) Date to) {

        List<Article> articles = articleRepository.filter(keyword, author, from, to);
        return getCollectionResponseEntity(articles);
    }

    private ResponseEntity<Collection<ArticleResource>> getCollectionResponseEntity(List<Article> articles) {
        if (articles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(articleResourceResolver.toResourceCollection(articles), HttpStatus.OK);
    }
}
