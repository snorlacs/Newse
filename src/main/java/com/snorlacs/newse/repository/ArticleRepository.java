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

    public List<Article> filter(String keyword, String author, Date from, Date to) {

        Predicate<Article> filterPredicate = keywordPredicate(keyword)
                .and(authorPredicate(author))
                .and(fromDatePredicate(from))
                .and(toDatePredicate(to));

        return findByPredicate(filterPredicate);
    }

    private Predicate<Article> keywordPredicate(String keyword) {
        return article -> keyword == null || article.getKeywords().contains(keyword);
    }

    private Predicate<Article> authorPredicate(String authorName) {
        return article -> authorName == null || article.getAuthors()
                .stream()
                .anyMatch(author -> author.getName().equals(authorName));
    }

    private Predicate<Article> fromDatePredicate(Date from) {
        return article -> from == null || article.getPublishedOn().equals(from) || article.getPublishedOn().after(from);
    }

    private Predicate<Article> toDatePredicate(Date to) {
        return article -> to == null || article.getPublishedOn().equals(to) || article.getPublishedOn().before(to);
    }
}
