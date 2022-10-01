package de.ar.openfree.schemaorg.jpa;

import de.ar.openfree.schemaorg.*;
import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import de.ar.openfree.schemaorg.jpa.property.JpaPropertyRepository;
import de.ar.openfree.schemaorg.jpa.type.JpaType;
import de.ar.openfree.schemaorg.jpa.type.JpaTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaRepositoryAdapter implements TypeRepository, PropertyRepository {

    private final JpaTypeRepository typeRepository;
    private final JpaPropertyRepository propertyRepository;

    public MutableType createType() {
        return mutateType(new JpaType());
    }

    public MutableType mutateType(Long id) {
        return mutateType(typeRepository.getReferenceById(id));
    }

    public MutableType mutateType(Type type) {
        var mutator = JpaMutator.newInstance(type, MutableType.class);
        ((ChangeSet) mutator).setChangeListener(changeSet -> {
            if (type.getLabel() == null) {
                log.error("Label is null", type);
            }
            typeRepository.save((JpaType) type);
            changeSet.setChangeListener(null);
        });
        return (MutableType) mutator;
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
        var mutator = JpaMutator.newInstance(property, MutableProperty.class);
        ((ChangeSet) mutator).setChangeListener(changeSet -> {
            if (property.getLabel() == null) {
                log.error("Label is null", property);
            }
            propertyRepository.save((JpaProperty) property);
            changeSet.setChangeListener(null);
        });
        return (MutableProperty) mutator;
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
