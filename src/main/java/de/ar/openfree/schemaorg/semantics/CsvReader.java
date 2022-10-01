package de.ar.openfree.schemaorg.semantics;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CsvReader {

    public List<String> readLines(URL url) {
        var mapper = new CsvMapper();
        try {
            MappingIterator<String> it = mapper.readerForListOf(String.class).with(CsvParser.Feature.WRAP_AS_ARRAY).readValues(url);
            return it.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> readWithColumns(URL url, String... columns) {
        return this.readWithColumns(url, Arrays.asList(columns));
    }

    public List<Map<String, String>> readWithColumns(URL url, Iterable<String> columns) {
        var schemaBuilder = CsvSchema.builder();
        columns.forEach(schemaBuilder::addColumn);

        var schema = schemaBuilder.build();
        var mapper = new CsvMapper();
        try {
            MappingIterator<Map<String, String>> it = mapper.readerForMapOf(String.class).with(schema).readValues(url);
            return it.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
