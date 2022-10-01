package de.ar.openfree.schemaorg.schema;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(indexes = {
        @Index(name = "schema_label_idx", columnList = "label", unique = true),
        @Index(name = "schema_url_idx", columnList = "url", unique = true)
})
public class Vocab extends Element {

    @OneToMany
    private List<Type> types;

    @OneToMany
    private List<Property> properties;

}
