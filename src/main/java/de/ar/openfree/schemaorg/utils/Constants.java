package de.ar.openfree.schemaorg.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public final static List<String> TYPE_COLUMNS = Arrays.asList("id", "label", "comment", "subTypeOf", "enumerationtype",
            "equivalentClass", "properties", "subTypes", "supersedes", "supersededBy", "isPartOf");

    public final static List<String> PROPERTY_COLUMNS = Arrays.asList(
            "id", "label", "comment", "subPropertyOf", "equivalentProperty", "subproperties", "domainIncludes",
            "rangeIncludes", "inverseOf", "supersedes", "supersededBy", "isPartOf");
    public final static String ROOT_DIR = "C:/Users/RM04/Desktop/openfree/openfree-services/";
    public final static String OUTPUT_URL = ROOT_DIR + "output/schemaorg.xmi";
    public final static String TYPES_URL = "file:///" + ROOT_DIR + "data/types.csv";
    public final static String PROPERTIES_URL = "file:///" + ROOT_DIR + "data/properties.csv";

    public static final String SEMANTIC_ECORE = "file:///" + ROOT_DIR + "model/semantics.ecore";
}
