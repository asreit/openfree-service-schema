package de.ar.openfree.schemaorg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ar.openfree.schemaorg.schema.Element;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;

//@JsonIncludeProperties("links")
public class ElementListItemModel extends RepresentationModel<ElementListItemModel> {
    @JsonIgnore
    private final Element element;

    public ElementListItemModel(@NonNull Element element) {
        this.element = element;
    }


    @JsonIgnore
    public Element getElement() {
        return element;
    }

    @JsonIgnore
    public Class<? extends Element> getInstanceClass() {
        return element.getClass();
    }

    @JsonIgnore
    public Long getId() {
        return element.getId();
    }

    @JsonIgnore
    public String getUrl() {
        return element.getUrl();
    }
    
    @JsonIgnore
    public String getLabel() {
        return element.getLabel();
    }
}
