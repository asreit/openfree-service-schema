package de.ar.openfree.schemaorg.repository;

import de.ar.openfree.schemaorg.schema.Element;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ElementRepository<T extends Element> {
    Optional<T> findByLabel(String label);

    Optional<T> findByUrl(String url);
}
