package de.ar.openfree.schemaorg.model;

import de.ar.openfree.schemaorg.schema.Element;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ElementListAssembler  {

    private final EntityLinks entityLinks;
    
    public ElementListItemModel toModel(@NonNull Element entity) {
        var resource = new ElementListItemModel(entity);
        addLinks(resource);
        return resource;
    }
    
    public ElementListModel toCollectionModel(@NonNull Iterable<Element> entities) {
        var resourceList = new ArrayList<ElementListItemModel>();
        var iter = entities.iterator();
        
        while(iter.hasNext()) {
            var entity = iter.next();
            resourceList.add(toModel(entity));
        }
        
        var resources = new ElementListModel(resourceList);
        // addLinks(resources);
        return resources;
    }
    
    public void addLinks(ElementListItemModel resource) {
        var self = entityLinks.linkToItemResource(resource.getInstanceClass(), resource.getId())
                .withSelfRel()
                .withRel(resource.getUrl())
                .withTitle(resource.getLabel());
        resource.add(self);
    }
    
//    public void addLinks(ElementListModel resources) {
//        var self = Link
//                .of(ServletUriComponentsBuilder.fromCurrentRequest().toUriString())
//                .withSelfRel();
//        resources.add(self);
//    }
    
}
