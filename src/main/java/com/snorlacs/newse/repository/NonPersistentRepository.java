package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class NonPersistentRepository<T extends Identifiable> {

    @Autowired
    private IdGenerator idGenerator;

    private List<T> articles = Collections.synchronizedList(new ArrayList<>());

    public T create(T article) {
        articles.add(article);
        article.setId(idGenerator.getId());
        return article;
    }

    public void clear() {
        articles.clear();
    }

    public int getCount() {
        return articles.size();
    }
}
