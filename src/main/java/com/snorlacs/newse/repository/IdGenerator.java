package com.snorlacs.newse.repository;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IdGenerator {
    private AtomicLong id = new AtomicLong(1);

    public long getId() {
        return id.getAndIncrement();
    }
}
