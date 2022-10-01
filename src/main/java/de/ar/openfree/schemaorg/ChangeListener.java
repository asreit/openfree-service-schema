package de.ar.openfree.schemaorg;

import java.util.EventListener;

@FunctionalInterface
public interface ChangeListener extends EventListener {

    void onChange(ChangeSet changeSet);

}
