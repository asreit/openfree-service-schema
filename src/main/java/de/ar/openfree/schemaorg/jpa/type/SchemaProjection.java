package de.ar.openfree.schemaorg.jpa.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.ar.openfree.schemaorg.Property;
import de.ar.openfree.schemaorg.jpa.element.JpaSchema;
import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Projection(name = "schema", types = {JpaType.class})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({"$schema", "$id", "type", "title", "desc"})
public interface SchemaProjection {
    static Map<String, Property> getProperty(JpaSchema schema) {
        if (schema instanceof JpaType) {
            return ((JpaType) schema).getProperties().stream()
                    .collect(Collectors.toMap(Property::getLabel, Function.identity()));
        }
        return null;
    }

    static Object getRangeIncludes(JpaSchema schema) {
        if (schema instanceof JpaProperty) {
            var ranges = ((JpaProperty) schema).getRangeIncludes().stream()
                    .map(de.ar.openfree.schemaorg.Schema::getLabel)
                    .collect(Collectors.toList());
            if (ranges.size() == 1) {
                return ranges.get(0);
            } else if (ranges.size() > 1) {
                return ranges;
            }
        }
        return null;
    }

    @Value("http://json-schema.org/draft-04/schema#")
    @JsonProperty("$schema")
    String getSchema();

    @JsonProperty("$id")
    String getUrl();

    @Value("object")
    String getType();

    @JsonProperty("title")
    String getLabel();

    String getComment();

    @Value("#{T(de.ar.openfree.schemaorg.jpa.type.SchemaProjection).getProperty(target)}")
    Map<String, Schema> getProperties();

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonPropertyOrder({"type", "title", "desc", "properties"})
    interface Schema {
        @Value("#{T(de.ar.openfree.schemaorg.jpa.type.SchemaProjection).getRangeIncludes(target)}")
        Object getType();

        @JsonProperty("title")
        String getLabel();

        String getComment();

        @Value("#{T(de.ar.openfree.schemaorg.jpa.type.SchemaProjection).getProperty(target)}")
        Map<String, Schema> getProperties();
    }

}

