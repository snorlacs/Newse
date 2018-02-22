package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository extends NonPersistentRepository<Article> {
}
