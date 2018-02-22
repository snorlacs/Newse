package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class NonPersistentRepository<T extends Identifiable> {

    @Autowired
    private IdGenerator idGenerator;

    private List<T> articles = new ArrayList<>();

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

    public Optional<T> findById(Long id) {
        return articles.stream()
                .filter(article -> article.getId().equals(id))
                .findFirst();
    }

    public boolean delete(Long id) {
        return articles.removeIf(article -> article.getId().equals(id));
    }

}
