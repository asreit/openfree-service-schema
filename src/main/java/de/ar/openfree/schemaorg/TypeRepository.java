package de.ar.openfree.schemaorg;

public interface TypeRepository {
    MutableType createType();

    MutableType mutateType(Long id);

    MutableType mutateType(Type type);

    boolean deleteType(Long id);
}
