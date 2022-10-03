package de.ar.openfree.schemaorg.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="properties", collectionResourceRel = "properties", itemResourceRel = "property")
public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
}
