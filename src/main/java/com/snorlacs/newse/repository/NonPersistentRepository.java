package com.snorlacs.newse.repository;

import com.snorlacs.newse.domain.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class NonPersistentRepository<T extends Identifiable> {

    @Autowired
    private IdGenerator idGenerator;

    private List<T> entities = new ArrayList<>();

    public T create(T entity) {
        entities.add(entity);
        entity.setId(idGenerator.getId());
        return entity;
    }

    public void clear() {
        entities.clear();
    }

    public int getCount() {
        return entities.size();
    }

    public Optional<T> findOne(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public boolean delete(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    public boolean update(Long id, T updatedEntity) {
        if (updatedEntity == null) {
            return false;
        } else {
            Optional<T> entity = findOne(id);
            entity.ifPresent(originalArticle -> updateIfExists(originalArticle, updatedEntity));
            return entity.isPresent();
        }
    }

    public List<T> findByPredicate(Predicate<T> predicate) {
        return entities.stream().filter(predicate).collect(Collectors.toList());
    }

    protected abstract void updateIfExists(T originalArticle, T updatedArticle);

}
