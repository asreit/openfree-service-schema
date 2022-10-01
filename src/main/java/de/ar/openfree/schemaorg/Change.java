package de.ar.openfree.schemaorg;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Change {
    private String property;
    private Object previousValue;
    @Setter
    private Object currentValue;
}
