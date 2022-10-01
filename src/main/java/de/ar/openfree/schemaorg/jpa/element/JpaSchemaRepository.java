package de.ar.openfree.schemaorg.jpa.element;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface JpaSchemaRepository<T extends JpaSchema> {

    Optional<T> findById(Long id);

    Optional<T> findByLabel(String label);

    Optional<T> findByUrl(String label);

    default Optional<T> findByLabel(String label, Class<T> cls) {
        return findByLabel(label);
    }

    default Optional<T> findByUrl(String url, Class<T> cls) {
        return findByUrl(url);
    }


    default Optional<T> findById(Long id, Class<T> cls) {
        return findById(id);
    }

}
