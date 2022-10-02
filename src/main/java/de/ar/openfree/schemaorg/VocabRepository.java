package de.ar.openfree.schemaorg;

public interface VocabRepository {
    MutableVocab createVocab();
    boolean deleteVocab(Long id);
}
