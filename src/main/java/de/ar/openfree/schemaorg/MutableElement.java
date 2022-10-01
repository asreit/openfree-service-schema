package de.ar.openfree.schemaorg;

public interface MutableElement<T extends MutableElement<T>> {

    T label(String label);

    T comment(String label);

    T url(String label);

}
