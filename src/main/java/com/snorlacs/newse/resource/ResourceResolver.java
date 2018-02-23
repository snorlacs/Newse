package com.snorlacs.newse.resource;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class ResourceResolver<Domain, Resource> {

    public abstract Resource toResource(Domain domainObject);

    public Collection<Resource> toResourceCollection(Collection<Domain> domainObjects) {
        return domainObjects.stream().map(this::toResource).collect(Collectors.toList());
    }
}