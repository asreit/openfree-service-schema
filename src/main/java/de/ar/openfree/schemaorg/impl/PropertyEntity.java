package de.ar.openfree.schemaorg.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Entity
@Table(name="property")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyEntity {

    @ToString.Include
    private String id;

    @Id
    private String label;

    @Lob
    private String comment;

    private String equivalentProperty;

    @ManyToMany(mappedBy = "subproperties")
    private Set<PropertyEntity> subPropertyOf = new HashSet<>();

    @ManyToMany
    private Set<PropertyEntity> subproperties = new HashSet<>();

    @ManyToMany
    private Set<TypeEntity> domainIncludes = new HashSet<>();

    @ManyToMany
    private Set<TypeEntity> rangeIncludes = new HashSet<>();

    @ManyToOne
    private PropertyEntity inverseOf;

    @ManyToMany(mappedBy = "supersededBy")
    private Set<PropertyEntity> supersedes = new HashSet<>();

    @ManyToMany
    private Set<PropertyEntity> supersededBy = new HashSet<>();

    private String subPropertyOfUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PropertyEntity that = (PropertyEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
