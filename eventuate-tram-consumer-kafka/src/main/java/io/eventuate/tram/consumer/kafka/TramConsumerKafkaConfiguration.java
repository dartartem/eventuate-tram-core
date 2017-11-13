package io.eventuate.tram.consumer.kafka;

import io.eventuate.local.java.kafka.EventuateKafkaConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(EventuateKafkaConfigurationProperties.class)
public class TramConsumerKafkaConfiguration {

  @Value("${eventuateLocal.cdc.eventuate.database:#{\"eventuate\"}}")
  private String eventuateDatabase;

  @Bean
  public MessageConsumerKafkaImpl messageConsumerKafka(EventuateKafkaConfigurationProperties props) {
    return new MessageConsumerKafkaImpl(props.getBootstrapServers());
  }

  @Bean
  public DuplicateMessageDetector duplicateMessageDetector() {
    return new DuplicateMessageDetector(eventuateDatabase);
  }
}
