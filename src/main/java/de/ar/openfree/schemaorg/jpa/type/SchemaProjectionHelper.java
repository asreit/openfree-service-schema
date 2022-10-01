package de.ar.openfree.schemaorg.jpa.type;

import de.ar.openfree.schemaorg.Property;
import de.ar.openfree.schemaorg.Schema;
import de.ar.openfree.schemaorg.jpa.element.JpaSchema;
import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SchemaProjectionHelper {
    public Map<String, Property> getProperty(JpaSchema schema) {
        if (schema instanceof JpaType) {
            return ((JpaType) schema).getProperties().stream()
                    .collect(Collectors.toMap(Property::getLabel, Function.identity()));
        }
        return null;
    }

    public Object getRangeIncludes(JpaSchema schema) {
        if (schema instanceof JpaProperty) {
            var ranges = ((JpaProperty) schema).getRangeIncludes().stream()
                    .map(Schema::getLabel)
                    .collect(Collectors.toList());
            if (ranges.size() == 1) {
                return ranges.get(0);
            } else if (ranges.size() > 1) {
                return ranges;
            }
        }
        return null;
    }
}
