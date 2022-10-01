package de.ar.openfree.schemaorg.schema;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class Element {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String url;

    @NotNull
    @Column(nullable = false)
    private String label;

    @Lob
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Element element = (Element) o;
        return id != null && Objects.equals(id, element.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
