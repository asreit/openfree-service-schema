package de.ar.openfree.schemaorg.schema;

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
public class Property extends Element {

    private String equivalentProperty;

    @ManyToOne
    private Property subPropertyOf;

    @OneToMany(mappedBy = "subPropertyOf")
    private List<Property> subproperties;

    @ManyToOne
    private Type domainIncludes;

    @ManyToMany
    private List<Type> rangeIncludes;

    @OneToOne
    private Property inverseOf;

    @ManyToOne
    private Property supersedes;

    @OneToMany(mappedBy = "supersedes")
    private List<Property> supersededBy;

    private String isPartOfString;
}
