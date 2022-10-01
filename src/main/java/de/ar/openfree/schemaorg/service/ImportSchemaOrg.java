package de.ar.openfree.schemaorg.service;

import de.ar.openfree.schemaorg.repository.PropertyRepository;
import de.ar.openfree.schemaorg.repository.TypeRepository;
import de.ar.openfree.schemaorg.schema.Property;
import de.ar.openfree.schemaorg.schema.Type;
import de.ar.openfree.schemaorg.semantics.CsvReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class ImportSchemaOrg implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final TypeRepository typeRepository;

    private final List<String> typeColumns = Arrays.asList("id", "label", "comment", "subTypeOf", "enumerationtype",
            "equivalentClass", "properties", "subTypes", "supersedes", "supersededBy", "isPartOf");
    private final List<String> propColumns = Arrays.asList("id", "label", "comment", "subPropertyOf", "equivalentProperty",
            "subproperties", "domainIncludes", "rangeIncludes", "inverseOf", "supersedes", "supersededBy", "isPartOf");

    @Value("${openfree.storage.schemaorg.csv.types:classpath:schema.org/types.csv}")
    private URL typesResourceURL;

    @Value("${openfree.storage.schemaorg.csv.properties:classpath:schema.org/properties.csv}")
    private URL propsResourceURL;

    @Override
    public void run(String... args) throws Exception {
        log.info("Import Schema.org... [types={},properties={}]", typesResourceURL, propsResourceURL);

        var startTime = Instant.now();
        log.info("Create Schema.org properties... [url={}]", propsResourceURL);

        var props = readCSV(propsResourceURL, propColumns);
        props.remove(0);

        var propertyMap = new HashMap<String, Property>();
        for (var data : props) {
            var property = Property.builder()
                    .comment(data.get("comment"))
                    .equivalentProperty(mapToValue(data, "equivalentProperty"))
                    .isPartOfString(mapToValue(data, "isPartOf"))
                    .label(mapToValue(data, "label"))
                    .url(mapToValue(data, "id"))
                    .build();
            propertyMap.put(property.getUrl(), propertyRepository.save(property));
        }

        log.info("Create Schema.org types... [url={}]", typesResourceURL);

        var types = readCSV(typesResourceURL, typeColumns);
        types.remove(0);

        var typeMap = new HashMap<String, Type>();
        for (var data : types) {
            var type = Type.builder()
                    .comment(mapToValue(data, "comment"))
                    .equivalentClass(mapToValue(data, "equivalentProperty"))
                    .isPartOfString(mapToValue(data, "isPartOf"))
                    .label(mapToValue(data, "label"))
                    .url(mapToValue(data, "id"))
                    .build();
            typeMap.put(type.getUrl(), typeRepository.save(type));
        }

        log.info("Linking Properties...");
        for (var data : props) {
            var prop = propertyMap.get(data.get("id"));
            prop.setDomainIncludes(mapTypeReference(typeMap, data.get("domainIncludes")));
            prop.setInverseOf(mapPropReference(propertyMap, data.get("inverseOf")));
            prop.setRangeIncludes(mapTypeReferences(typeMap, data.get("rangeIncludes")));
            prop.setSubproperties(mapPropReferences(propertyMap, data.get("subProperties")));
            prop.setSubPropertyOf(mapPropReference(propertyMap, data.get("subPropertyOf")));
            prop.setSupersededBy(mapPropReferences(propertyMap, data.get("supersededBy")));
            prop.setSupersedes(mapPropReference(propertyMap, data.get("supersedes")));
            propertyRepository.save(prop);
        }

        log.info("Linking Types...");
        for (var data : types) {
            var type = typeMap.get(data.get("id"));
            type.setEnumerationtype(typeMap.get(data.get("enumerationtype")));
            type.setProperties(mapPropReferences(propertyMap, data.get("properties")));
            type.setSubTypeOf(mapTypeReference(typeMap, data.get("subTypeOf")));
            type.setSubTypes(mapTypeReferences(typeMap, data.get("subTypes")));
            type.setSupersededBy(mapTypeReferences(typeMap, data.get("supersededBy")));
            type.setSupersedes(mapTypeReference(typeMap, data.get("supersedes")));
            typeRepository.save(type);
        }

        var duration = Duration.between(startTime, Instant.now());
        log.info("Imported. [duration={}ms]", duration.toMillis());
    }

    private List<Map<String, String>> readCSV(URL url, Iterable<String> columns) {
        return new CsvReader().readWithColumns(url, columns);
    }
    
    private String mapToValue(Map<String, String> data, String s) {
        if(Strings.isBlank(s)) {
            return null;
        }
        
        var value = data.get(s);
        return Strings.isBlank(value) ? null : value.strip();
    }

    private Property mapPropReference(HashMap<String, Property> propertyMap, String s) {
        var properties = mapPropReferences(propertyMap, s);
        if (properties.size() > 1) {
            throw new RuntimeException("Sollte nur ein Property geben: " + s);
        }
        return properties.stream().findFirst().orElse(null);
    }

    private List<Property> mapPropReferences(HashMap<String, Property> propertyMap, String s) {
        if (Strings.isBlank(s)) {
            return Collections.emptyList();
        }

        return Arrays.stream(s.split(" "))
                .map(String::strip)
                .map(propertyMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Type> mapTypeReferences(HashMap<String, Type> typeMap, String s) {
        if (Strings.isBlank(s)) {
            return Collections.emptyList();
        }
        
        return Arrays.stream(s.split(" "))
                .map(String::strip)
                .map(typeMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Type mapTypeReference(HashMap<String, Type> typeMap, String s) {
        var types = mapTypeReferences(typeMap, s);
        if (types.size() > 1) {
            throw new RuntimeException("Sollte nur ein Element geben: " + s);
        }
        return types.stream().findFirst().orElse(null);
    }

}
