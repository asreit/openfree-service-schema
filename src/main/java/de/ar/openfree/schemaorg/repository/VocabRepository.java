package de.ar.openfree.schemaorg.repository;

import de.ar.openfree.schemaorg.schema.Schema;
import de.ar.openfree.schemaorg.schema.Vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = Schema.class)
public interface VocabRepository extends JpaRepository<Vocab, Long>, ElementRepository<Vocab> {
}
