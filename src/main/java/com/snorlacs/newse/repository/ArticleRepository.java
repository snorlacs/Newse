package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Article;
import org.springframework.stereotype.Repository;

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
}
