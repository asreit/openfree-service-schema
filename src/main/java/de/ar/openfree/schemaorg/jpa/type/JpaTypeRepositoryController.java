package de.ar.openfree.schemaorg.jpa.type;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RequiredArgsConstructor
@RepositoryRestController
public class JpaTypeRepositoryController implements RepresentationModelProcessor<EntityModel<JpaType>> {

    private final JpaTypeRepository repository;

    @GetMapping("/types/{label}/allProperties")
    public ResponseEntity<?> getAllProperties(@PathVariable("label") String label, PersistentEntityResourceAssembler assembler) {
        return repository.findByLabel(label)
                .map(JpaType::getAllProperties)
                .map(assembler::toCollectionModel)
                .map(model -> model.add(Link.of(fromCurrentRequest().toUriString()).withSelfRel()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public EntityModel<JpaType> process(EntityModel<JpaType> model) {
        return model.add(linkTo(methodOn(getClass(), model.getContent().getLabel())
                .getAllProperties(null, null))
                .withRel("allProperties"));
    }

}
