package de.ar.openfree.schemaorg.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="schema", types = Element.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({"url", "label", "comment"})
public interface Schema {
    @JsonProperty("id")
    String getUrl();

    @JsonProperty("type")
    String getLabel();

    String getComment();
}
