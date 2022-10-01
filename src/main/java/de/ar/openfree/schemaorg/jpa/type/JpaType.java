package de.ar.openfree.schemaorg.jpa.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.ar.openfree.schemaorg.Property;
import de.ar.openfree.schemaorg.Type;
import de.ar.openfree.schemaorg.jpa.element.JpaSchema;
import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(indexes = {
        @Index(name = "type_label_idx", columnList = "label", unique = true),
        @Index(name = "type_url_idx", columnList = "url", unique = true)
})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JpaType extends JpaSchema implements Type {

    @OneToOne(targetEntity = JpaType.class)
    private Type enumerationtype;

    private String equivalentClass;

    @OneToMany(mappedBy = "domainIncludes", targetEntity = JpaProperty.class)
    private List<Property> properties;

    @ManyToOne(targetEntity = JpaType.class)
    private Type subTypeOf;

    @OneToMany(mappedBy = "subTypeOf", targetEntity = JpaType.class)
    private List<Type> subTypes;

    @ManyToOne(targetEntity = JpaType.class)
    private Type supersedes;

    @OneToMany(mappedBy = "supersedes", targetEntity = JpaType.class)
    private List<Type> supersededBy;

    private String isPartOfString;

    @Override
    @JsonIgnore
    public List<Property> getAllProperties() {
        var allProperties = getProperties().stream()
                .sorted(Comparator.comparing(Property::getLabel))
                .collect(Collectors.toList());

        if (getSubTypeOf() == null) {
            return allProperties;
        }

        allProperties.addAll(getSubTypeOf().getAllProperties());
        return allProperties;
    }
}
