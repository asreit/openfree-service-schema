package de.ar.openfree.schemaorg;

import java.util.List;

public interface MutableProperty extends Mutable<Property>, MutableElement<MutableProperty> {

    MutableProperty equivalentProperty(String equivalentProperty);

    MutableProperty subPropertyOf(Property subPropertyOf);

    MutableProperty subproperties(List<Property> subproperties);

    MutableProperty domainIncludes(Type domainIncludes);

    MutableProperty rangeIncludes(List<Type> rangeIncludes);

    MutableProperty inverseOf(Property inverseOf);

    MutableProperty supersedes(Property supersedes);

    MutableProperty supersededBy(List<Property> supersededBy);

    MutableProperty isPartOfString(String isPartOfString);

    MutableProperty vocab(Vocab vocab);

    Property save();

}
