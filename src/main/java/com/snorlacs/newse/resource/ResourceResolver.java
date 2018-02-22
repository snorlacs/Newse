package com.snorlacs.newse.resource;

public abstract class ResourceResolver<Domain, Resource> {

    public abstract Resource toResource(Domain domainObject);

}
