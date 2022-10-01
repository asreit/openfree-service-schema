package de.ar.openfree.schemaorg.repository;

import de.ar.openfree.schemaorg.schema.Property;
import de.ar.openfree.schemaorg.schema.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = Schema.class)
public interface PropertyRepository extends JpaRepository<Property, Long>, ElementRepository<Property> {
}
