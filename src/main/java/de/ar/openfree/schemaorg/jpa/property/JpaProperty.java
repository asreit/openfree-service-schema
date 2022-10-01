package de.ar.openfree.schemaorg.jpa.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.ar.openfree.schemaorg.Property;
import de.ar.openfree.schemaorg.Type;
import de.ar.openfree.schemaorg.jpa.element.JpaSchema;
import de.ar.openfree.schemaorg.jpa.type.JpaType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(indexes = {
        @Index(name = "property_label_idx", columnList = "label", unique = true),
        @Index(name = "property_url_idx", columnList = "url", unique = true)
})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JpaProperty extends JpaSchema implements Property {

    private String equivalentProperty;

    @ManyToOne(targetEntity = JpaProperty.class)
    private Property subPropertyOf;

    @OneToMany(mappedBy = "subPropertyOf", targetEntity = JpaProperty.class)
    private List<Property> subproperties;

    @ManyToOne(targetEntity = JpaType.class)
    private Type domainIncludes;

    @ManyToMany(targetEntity = JpaType.class)
    private List<Type> rangeIncludes;

    @OneToOne(targetEntity = JpaProperty.class)
    private Property inverseOf;

    @ManyToOne(targetEntity = JpaProperty.class)
    private Property supersedes;

    @OneToMany(mappedBy = "supersedes", targetEntity = JpaProperty.class)
    private List<Property> supersededBy;

    private String isPartOfString;
}
