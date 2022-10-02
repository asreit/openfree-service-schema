package de.ar.openfree.schemaorg.jpa;

import de.ar.openfree.schemaorg.*;
import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import de.ar.openfree.schemaorg.jpa.property.JpaPropertyRepository;
import de.ar.openfree.schemaorg.jpa.type.JpaType;
import de.ar.openfree.schemaorg.jpa.type.JpaTypeRepository;
import de.ar.openfree.schemaorg.jpa.vocab.JpaVocab;
import de.ar.openfree.schemaorg.jpa.vocab.JpaVocabRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaRepositoryAdapter implements VocabRepository, TypeRepository, PropertyRepository {

    private final JpaVocabRepository vocabRepository;
    private final JpaTypeRepository typeRepository;
    private final JpaPropertyRepository propertyRepository;

    public MutableVocab createVocab() {
        return mutableVocab(new JpaVocab());
    }

    @Override
    public boolean deleteVocab(Long id) {
        if(vocabRepository.existsById(id)) {
            vocabRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private MutableVocab mutableVocab(JpaVocab vocab) {
        return JpaMutator.newInstance(vocab,
                MutableVocab.class,
                changeSet -> vocabRepository.save(vocab));
    }

    public MutableType createType() {
        return mutateType(new JpaType());
    }

    public MutableType mutateType(Long id) {
        return mutateType(typeRepository.getReferenceById(id));
    }

    public MutableType mutateType(Type type) {
        return JpaMutator.newInstance(type,
                MutableType.class,
                changeSet -> typeRepository.save((JpaType) type));
    }

    public boolean deleteType(Long id) {
        if (typeRepository.existsById(id)) {
            typeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public MutableProperty createProperty() {
        return mutateProperty(new JpaProperty());
    }

    @Override
    public MutableProperty mutateProperty(Long id) {
        return mutateProperty(propertyRepository.getReferenceById(id));
    }

    @Override
    public MutableProperty mutateProperty(Property property) {
        return JpaMutator.newInstance(property,
                MutableProperty.class,
                changeSet -> propertyRepository.save((JpaProperty) property));
    }

    @Override
    public boolean deleteProperty(Long id) {
        if (propertyRepository.existsById(id)) {
            this.propertyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
