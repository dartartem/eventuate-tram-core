package io.eventuate.tram.cdc.mysql.connector;

import io.eventuate.javaclient.spring.jdbc.EventuateSchema;
import io.eventuate.local.common.CdcKafkaPublisher;
import io.eventuate.local.common.CdcProcessor;
import io.eventuate.local.common.EventuateConfigurationProperties;
import io.eventuate.local.common.PublishingStrategy;
import io.eventuate.local.java.kafka.EventuateKafkaConfigurationProperties;
import io.eventuate.local.polling.PollingCdcKafkaPublisher;
import io.eventuate.local.polling.PollingCdcProcessor;
import io.eventuate.local.polling.PollingDao;
import io.eventuate.local.polling.PollingDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("EventuatePolling")
public class PollingMessageTableChangesToDestinationsConfiguration {

  @Bean
  public CdcKafkaPublisher<MessageWithDestination> pollingCdcKafkaPublisher(EventuateKafkaConfigurationProperties eventuateKafkaConfigurationProperties,
          PublishingStrategy<MessageWithDestination> publishingStrategy) {

    return new PollingCdcKafkaPublisher<>(eventuateKafkaConfigurationProperties.getBootstrapServers(), publishingStrategy);
  }

  @Bean
  public CdcProcessor<MessageWithDestination> pollingCdcProcessor(EventuateConfigurationProperties eventuateConfigurationProperties,
    PollingDao<PollingMessageBean, MessageWithDestination, String> pollingDao) {

    return new PollingCdcProcessor<>(pollingDao, eventuateConfigurationProperties.getPollingIntervalInMilliseconds());
  }

  @Bean
  public PollingDao<PollingMessageBean, MessageWithDestination, String> pollingDao(PollingDataProvider<PollingMessageBean, MessageWithDestination, String> pollingDataProvider,
    DataSource dataSource,
    EventuateConfigurationProperties eventuateConfigurationProperties) {

    return new PollingDao<>(pollingDataProvider,
      dataSource,
      eventuateConfigurationProperties.getMaxEventsPerPolling(),
      eventuateConfigurationProperties.getMaxAttemptsForPolling(),
      eventuateConfigurationProperties.getPollingRetryIntervalInMilliseconds());
  }

  @Bean
  public PollingDataProvider<PollingMessageBean, MessageWithDestination, String> pollingDataProvider(EventuateSchema eventuateSchema,
          EventuateConfigurationProperties eventuateConfigurationProperties) {
    return new PollingMessageDataProvider(eventuateSchema);
  }
}
