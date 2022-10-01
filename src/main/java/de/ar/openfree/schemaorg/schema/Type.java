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
        @Index(name = "type_label_idx", columnList = "label", unique = true),
        @Index(name = "type_url_idx", columnList = "url", unique = true)
})
public class Type extends Element {

    @OneToOne
    private Type enumerationtype;

    private String equivalentClass;

    @OneToMany(mappedBy = "domainIncludes")
    private List<Property> properties;

    @ManyToOne
    private Type subTypeOf;

    @OneToMany(mappedBy = "subTypeOf")
    private List<Type> subTypes;

    @ManyToOne
    private Type supersedes;

    @OneToMany(mappedBy = "supersedes")
    private List<Type> supersededBy;

    private String isPartOfString;
}
