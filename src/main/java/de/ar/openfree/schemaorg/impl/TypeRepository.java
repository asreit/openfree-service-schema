package de.ar.openfree.schemaorg.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="types", collectionResourceRel = "types", itemResourceRel = "type")
public interface TypeRepository extends JpaRepository<TypeEntity, String> {
}
