package de.ar.openfree.schemaorg.jpa.vocab;

import de.ar.openfree.schemaorg.Property;
import de.ar.openfree.schemaorg.Type;
import de.ar.openfree.schemaorg.Vocab;
import de.ar.openfree.schemaorg.jpa.element.JpaSchema;
import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import de.ar.openfree.schemaorg.jpa.type.JpaType;
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
public class JpaVocab extends JpaSchema implements Vocab {

    @OneToMany(mappedBy = "vocab", targetEntity = JpaProperty.class)
    private List<Type> types;

    @OneToMany(mappedBy = "vocab", targetEntity = JpaProperty.class)
    private List<Property> properties;

}
