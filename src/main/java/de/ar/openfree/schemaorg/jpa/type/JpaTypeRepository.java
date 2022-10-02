package de.ar.openfree.schemaorg.jpa.type;

import de.ar.openfree.schemaorg.jpa.element.JpaSchemaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "types", collectionResourceRel = "types", itemResourceRel = "type")
public interface JpaTypeRepository extends JpaRepository<JpaType, Long>, JpaSchemaRepository<JpaType> {

    Page<JpaType> findByVocabLabel(String label, Pageable pageable);

    @Query("select t from JpaType t where t.subTypeOf is null")
    Page<JpaType> findBaseTypes(Pageable pageable);

}
