package de.ar.openfree.schemaorg.jpa.property;

import de.ar.openfree.schemaorg.jpa.element.JpaSchemaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "properties", collectionResourceRel = "properties", itemResourceRel = "property")
public interface JpaPropertyRepository extends JpaRepository<JpaProperty, Long>, JpaSchemaRepository<JpaProperty> {

    Page<JpaProperty> findByVocabLabel(String label, Pageable pageable);

}
