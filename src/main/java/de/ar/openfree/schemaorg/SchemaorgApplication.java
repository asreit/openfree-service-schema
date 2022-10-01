package de.ar.openfree.schemaorg;


import de.ar.openfree.schemaorg.repository.PropertyRepository;
import de.ar.openfree.schemaorg.repository.VocabRepository;
import de.ar.openfree.schemaorg.repository.TypeRepository;
import de.ar.openfree.schemaorg.schema.Property;
import de.ar.openfree.schemaorg.schema.Vocab;
import de.ar.openfree.schemaorg.schema.Type;
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
                        .forRepository(PropertyRepository.class, Property::getLabel, PropertyRepository::findByLabel)
                        .forRepository(VocabRepository.class, Vocab::getLabel, VocabRepository::findByLabel)
                        .forRepository(TypeRepository.class, Type::getLabel, TypeRepository::findByLabel);
            }
        };
    }
}
