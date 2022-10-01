package de.ar.openfree.schemaorg.jpa.vocab;

import de.ar.openfree.schemaorg.Schema;
import de.ar.openfree.schemaorg.jpa.element.JpaSchemaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = Schema.class,
        path = "vocabs", collectionResourceRel = "vocabs", itemResourceRel = "vocab")
public interface JpaVocabRepository extends JpaRepository<JpaVocab, Long>, JpaSchemaRepository<JpaVocab> {
}
