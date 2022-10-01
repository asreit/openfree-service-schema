package de.ar.openfree.schemaorg;

public interface PropertyRepository {
    MutableProperty createProperty();

    MutableProperty mutateProperty(Long id);

    MutableProperty mutateProperty(Property property);

    boolean deleteProperty(Long id);
}
