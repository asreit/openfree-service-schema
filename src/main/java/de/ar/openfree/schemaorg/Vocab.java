package de.ar.openfree.schemaorg;

import java.util.List;

public interface Vocab extends Schema {
    List<Property> getProperties();
    List<Type> getTypes();
}
