package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Repository
public class ArticleRepository extends NonPersistentRepository<Article> {

    @Override
    protected void updateIfExists(Article originalArticle, Article updatedArticle) {
        originalArticle.setHeader(updatedArticle.getHeader());
        originalArticle.setShortDescription(updatedArticle.getShortDescription());
        originalArticle.setText(updatedArticle.getText());
        originalArticle.setPublishedOn(updatedArticle.getPublishedOn());
        originalArticle.setKeywords(updatedArticle.getKeywords());
        originalArticle.setAuthors(updatedArticle.getAuthors());
    }

    public List<Article> findByKeyword(String keyword) {
        Predicate<Article> articlePredicate = article -> article.getKeywords().contains(keyword);
        return findByField(articlePredicate);
    }

    public List<Article> findByAuthorName(String authorName) {
        Predicate<Article> articlePredicate = article -> article.getAuthors()
                .stream()
                .anyMatch(author -> author.getName().equals(authorName));

        return findByField(articlePredicate);
    }

    public List<Article> findByPublishedOn(Date from, Date to) {
        Predicate<Article> articlePredicate = article ->
                (article.getPublishedOn().after(from) && article.getPublishedOn().before(to)) ||
                (article.getPublishedOn().equals(from)) ||
                (article.getPublishedOn().equals(to));

        return findByField(articlePredicate);
    }
}
