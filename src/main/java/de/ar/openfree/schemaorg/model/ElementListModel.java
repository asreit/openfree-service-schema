package de.ar.openfree.schemaorg.model;

import java.util.ArrayList;
import java.util.Collection;

public class ElementListModel extends ArrayList<ElementListItemModel> {

    public ElementListModel() {
        super();
    }

    public ElementListModel(Collection<ElementListItemModel> content) {
        super(content);
    }
    
}
