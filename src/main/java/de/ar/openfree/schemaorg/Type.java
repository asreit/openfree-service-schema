package de.ar.openfree.schemaorg;

import java.util.List;

public interface Type extends Schema {
    String getIsPartOfString();

    Type getEnumerationtype();

    String getEquivalentClass();

    List<Property> getProperties();

    List<Property> getAllProperties();

    Type getSubTypeOf();

    List<Type> getSubTypes();

    Type getSupersedes();

    List<Type> getSupersededBy();
}
