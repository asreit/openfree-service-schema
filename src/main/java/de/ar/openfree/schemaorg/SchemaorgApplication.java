package de.ar.openfree.schemaorg;


import de.ar.openfree.schemaorg.jpa.property.JpaProperty;
import de.ar.openfree.schemaorg.jpa.property.JpaPropertyRepository;
import de.ar.openfree.schemaorg.jpa.type.JpaType;
import de.ar.openfree.schemaorg.jpa.type.JpaTypeRepository;
import de.ar.openfree.schemaorg.jpa.vocab.JpaVocab;
import de.ar.openfree.schemaorg.jpa.vocab.JpaVocabRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@SpringBootApplication
public class SchemaorgApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchemaorgApplication.class, args);
    }

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                config.withEntityLookup()
                        .forRepository(JpaPropertyRepository.class, JpaProperty::getLabel, JpaPropertyRepository::findByLabel)
                        .forRepository(JpaTypeRepository.class, JpaType::getLabel, JpaTypeRepository::findByLabel)
                        .forRepository(JpaVocabRepository.class, JpaVocab::getLabel, JpaVocabRepository::findByLabel);
            }
        };
    }

}
