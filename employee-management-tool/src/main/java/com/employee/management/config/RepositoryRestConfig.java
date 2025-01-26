package com.employee.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;

@Configuration
public class RepositoryRestConfig {
// TODO no benifit of this file need to check and remove still see profile-controller users-entity-controller in swagger UI

  @Bean
  public RepositoryRestConfiguration repositoryRestConfiguration(
      RepositoryRestConfiguration config) {
    // Only expose repositories explicitly annotated with @RepositoryRestResource
    config.setRepositoryDetectionStrategy(
        RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
    return config;
  }
}
