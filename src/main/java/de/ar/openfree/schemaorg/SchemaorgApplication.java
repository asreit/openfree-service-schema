package de.ar.openfree.schemaorg;


import de.ar.openfree.schemaorg.impl.PropertyEntity;
import de.ar.openfree.schemaorg.impl.TypeEntity;
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
                cors.addMapping("/**").allowedMethods("*").allowedOriginPatterns("*");
                config.exposeIdsFor(TypeEntity.class, PropertyEntity.class);
            }
        };
    }

}
