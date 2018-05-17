package io.eventuate.tram.cdc.status.service;

import io.eventuate.local.common.status.StatusService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class StatusServiceConfiguration {
  @Bean
  public StatusService statusService() {
    return new StatusService();
  }
}
