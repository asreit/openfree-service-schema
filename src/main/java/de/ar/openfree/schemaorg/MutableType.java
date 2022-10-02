package de.ar.openfree.schemaorg;

import java.util.List;

public interface MutableType extends Mutable<Type>, MutableElement<MutableType> {

    MutableType enumerationtype(Type enumerationtype);

    MutableType equivalentClass(String equivalentClass);

    MutableType properties(List<Property> properties);

    MutableType subTypeOf(Type subTypeOf);

    MutableType subTypes(List<Type> subTypes);

    MutableType supersedes(Type supersedes);

    MutableType supersededBy(List<Type> supersededBy);

    MutableType isPartOfString(String isPartOfString);

    MutableType vocab(Vocab vocab);

    Type save();
}
