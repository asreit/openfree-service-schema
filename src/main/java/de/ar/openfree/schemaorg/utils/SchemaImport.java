package de.ar.openfree.schemaorg.utils;

import de.ar.openfree.schemaorg.impl.PropertyEntity;
import de.ar.openfree.schemaorg.impl.PropertyRepository;
import de.ar.openfree.schemaorg.impl.TypeEntity;
import de.ar.openfree.schemaorg.impl.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchemaImport implements CommandLineRunner {

    private final List<String> typeColumns = Arrays.asList("id", "label", "comment", "subTypeOf", "enumerationtype",
            "equivalentClass", "properties", "subTypes", "supersedes", "supersededBy", "isPartOf");
    private final List<String> propColumns = Arrays.asList("id", "label", "comment", "subPropertyOf", "equivalentProperty",
            "subproperties", "domainIncludes", "rangeIncludes", "inverseOf", "supersedes", "supersededBy", "isPartOf");

    @Value("${openfree.storage.schemaorg.csv.types:classpath:schema.org/types.csv}")
    private URL typesResourceURL;

    @Value("${openfree.storage.schemaorg.csv.properties:classpath:schema.org/properties.csv}")
    private URL propsResourceURL;


    private final TypeRepository typeRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public void run(String... args) throws Exception {
        var csvReader = new CsvReader();

        var typeLines = csvReader.readWithColumns(typesResourceURL, typeColumns);
        var propertyLine = csvReader.readWithColumns(propsResourceURL, propColumns);

        var typeMap = new HashMap<String, TypeEntity>();
        var propMap = new HashMap<String, PropertyEntity>();

        for(var line : typeLines) {
            var type = new TypeEntity();
            type.setId(line.get("id"));
            type.setLabel(line.get("label"));
            type.setComment(line.get("comment"));
            type.setEquivalentClass(Optional.ofNullable(line.get("equivalentClass")).filter(s -> !Strings.isBlank(s)).orElse(null));
            typeMap.put(type.getId(), typeRepository.save(type));
        }

        for(var line : propertyLine) {
            var prop = new PropertyEntity();
            prop.setId(line.get("id"));
            prop.setLabel(line.get("label"));
            prop.setComment(line.get("comment"));
            prop.setEquivalentProperty(Optional.ofNullable(line.get("equivalentProperty")).filter(s -> !Strings.isBlank(s)).orElse(null));
            propMap.put(prop.getId(), propertyRepository.save(prop));
        }

        for(var line : typeLines) {
            var current = typeMap.get(line.get("id"));
            for(var id : split(line.get("subTypeOf"))) {
                var other = typeMap.get(id);
                if(other == null) {
                    System.out.println(current.getId() + " subTypeOf " + id + " cannot be mapped");
                    current.setSubTypeOfUrl(id.replace("http://schema.org/", ""));
                } else {
                    current.getSubTypeOf().add(other);
                    other.getSubTypes().add(current);
                }
            }

            for(var id : split(line.get("supersedes"))) {
                var other = typeMap.get(id);
                current.getSupersedes().add(other);
                other.getSupersededBy().add(current);
            }

            for(var id : split(line.get("enumerationtype"))) {
                var other = typeMap.get(id);
                current.setEnumerationtype(other);
            }
        }

        for(var line :propertyLine) {
            var current = propMap.get(line.get("id"));
            for(var id : split(line.get("subPropertyOf"))) {
                var other = propMap.get(id);
                if(other == null) {
                    System.out.println(current.getId() + " subPropertyOf " + id + " cannot be mapped.");
                    current.setSubPropertyOfUrl(id.replace("http://schema.org/", ""));
                } else {
                    current.getSubPropertyOf().add(other);
                    other.getSubproperties().add(current);
                }
            }

            for(var id : split(line.get("supersedes"))) {
                var other = propMap.get(id);
                current.getSupersedes().add(other);
                other.getSupersededBy().add(current);
            }

            for(var id : split(line.get("inverseOf"))) {
                var other = propMap.get(id);
                current.setInverseOf(other);
            }

            for(var id : split(line.get("rangeIncludes"))) {
                var type = typeMap.get(id);
                current.getRangeIncludes().add(type);
            }

            for(var id : split(line.get("domainIncludes"))) {
                var type = typeMap.get(id);
                current.getDomainIncludes().add(type);
                type.getProperties().add(current);
            }
        }

        typeRepository.saveAll(typeMap.values());
        propertyRepository.saveAll(propMap.values());
    }

    private List<String> split(String input) {
        if(Strings.isBlank(input)) {
            return Collections.emptyList();
        }
        return Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
