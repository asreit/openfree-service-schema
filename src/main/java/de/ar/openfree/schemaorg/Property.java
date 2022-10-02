package de.ar.openfree.schemaorg;

import java.util.List;

public interface Property extends Schema {
    @Override
    Long getId();

    @Override
    String getUrl();

    @Override
    String getLabel();

    @Override
    String getComment();

    String getEquivalentProperty();

    Property getSubPropertyOf();

    List<Property> getSubproperties();

    Type getDomainIncludes();

    List<Type> getRangeIncludes();

    Property getInverseOf();

    Property getSupersedes();

    List<Property> getSupersededBy();

    String getIsPartOfString();

    Vocab getVocab();
}
