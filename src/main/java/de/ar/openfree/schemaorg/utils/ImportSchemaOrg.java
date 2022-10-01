package de.ar.openfree.schemaorg.utils;

import de.ar.openfree.schemaorg.Property;
import de.ar.openfree.schemaorg.PropertyRepository;
import de.ar.openfree.schemaorg.Type;
import de.ar.openfree.schemaorg.TypeRepository;
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
            var property = propertyRepository.createProperty()
                    .comment(data.get("comment"))
                    .equivalentProperty(mapToValue(data, "equivalentProperty"))
                    .isPartOfString(mapToValue(data, "isPartOf"))
                    .label(mapToValue(data, "label"))
                    .url(mapToValue(data, "id"))
                    .save();
            propertyMap.put(property.getUrl(), property);
        }

        log.info("Create Schema.org types... [url={}]", typesResourceURL);

        var types = readCSV(typesResourceURL, typeColumns);
        types.remove(0);

        var typeMap = new HashMap<String, Type>();
        for (var data : types) {
            var type = typeRepository.createType()
                    .comment(mapToValue(data, "comment"))
                    .equivalentClass(mapToValue(data, "equivalentProperty"))
                    .isPartOfString(mapToValue(data, "isPartOf"))
                    .label(mapToValue(data, "label"))
                    .url(mapToValue(data, "id"))
                    .save();
            typeMap.put(type.getUrl(), type);
        }

        log.info("Linking Properties...");
        for (var data : props) {
            var prop = propertyMap.get(data.get("id"));
            propertyRepository.mutateProperty(prop)
                    .domainIncludes(mapTypeReference(typeMap, data.get("domainIncludes")))
                    .inverseOf(mapPropReference(propertyMap, data.get("inverseOf")))
                    .rangeIncludes(mapTypeReferences(typeMap, data.get("rangeIncludes")))
                    .subproperties(mapPropReferences(propertyMap, data.get("subProperties")))
                    .subPropertyOf(mapPropReference(propertyMap, data.get("subPropertyOf")))
                    .supersededBy(mapPropReferences(propertyMap, data.get("supersededBy")))
                    .supersedes(mapPropReference(propertyMap, data.get("supersedes")))
                    .save();
        }

        log.info("Linking Types...");
        for (var data : types) {
            var type = typeMap.get(data.get("id"));
            typeRepository.mutateType(type)
                    .enumerationtype(typeMap.get(data.get("enumerationtype")))
                    .properties(mapPropReferences(propertyMap, data.get("properties")))
                    .subTypeOf(mapTypeReference(typeMap, data.get("subTypeOf")))
                    .subTypes(mapTypeReferences(typeMap, data.get("subTypes")))
                    .supersededBy(mapTypeReferences(typeMap, data.get("supersededBy")))
                    .supersedes(mapTypeReference(typeMap, data.get("supersedes")))
                    .save();
        }

        var duration = Duration.between(startTime, Instant.now());
        log.info("Imported. [duration={}ms]", duration.toMillis());
    }

    private List<Map<String, String>> readCSV(URL url, Iterable<String> columns) {
        return new CsvReader().readWithColumns(url, columns);
    }

    private String mapToValue(Map<String, String> data, String s) {
        if (Strings.isBlank(s)) {
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
