package de.ar.openfree.schemaorg;

import java.util.List;

public interface ChangeSet {
    Schema getSchema();

    List<Change> getChanges();

    void setChangeListener(ChangeListener listener);
}
