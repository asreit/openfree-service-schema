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
@Table(name="type")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TypeEntity {

    @ToString.Include
    private String id;

    @Id
    private String label;

    @Lob
    private String comment;

    @ManyToMany(mappedBy = "subTypes")
    private Set<TypeEntity> subTypeOf = new HashSet<>();

    @ManyToMany
    private Set<TypeEntity> subTypes = new HashSet<>();

    @ManyToOne
    private TypeEntity enumerationtype;

    private String equivalentClass;

    @ManyToMany(mappedBy = "domainIncludes")
    private Set<PropertyEntity> properties = new HashSet<>();

    @ManyToMany(mappedBy = "supersededBy")
    private Set<TypeEntity> supersedes = new HashSet<>();

    @ManyToMany
    private Set<TypeEntity> supersededBy = new HashSet<>();

    private String subTypeOfUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TypeEntity that = (TypeEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
