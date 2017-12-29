package io.eventuate.tram.cdc.mysql.connector;

import io.eventuate.local.common.CdcKafkaPublisher;
import io.eventuate.local.common.CdcProcessor;
import io.eventuate.local.common.EventuateConfigurationProperties;
import io.eventuate.local.common.PublishingStrategy;
import io.eventuate.local.db.log.common.DatabaseOffsetKafkaStore;
import io.eventuate.local.db.log.common.DbLogBasedCdcKafkaPublisher;
import io.eventuate.local.db.log.common.DbLogBasedCdcProcessor;
import io.eventuate.local.db.log.common.DbLogClient;
import io.eventuate.local.java.kafka.EventuateKafkaConfigurationProperties;
import io.eventuate.local.java.kafka.producer.EventuateKafkaProducer;
import io.eventuate.local.mysql.binlog.DebeziumBinlogOffsetKafkaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!EventuatePolling")
public class DBLogCommonMessageTableChangesToDestinationsConfiguration {

  @Bean
  public DebeziumBinlogOffsetKafkaStore debeziumBinlogOffsetKafkaStore(EventuateConfigurationProperties eventuateConfigurationProperties,
          EventuateKafkaConfigurationProperties eventuateKafkaConfigurationProperties) {

    return new DebeziumBinlogOffsetKafkaStore(eventuateConfigurationProperties.getOldDbHistoryTopicName(), eventuateKafkaConfigurationProperties);
  }

  @Bean
  public DatabaseOffsetKafkaStore databaseOffsetKafkaStore(EventuateConfigurationProperties eventuateConfigurationProperties,
                                                           EventuateKafkaConfigurationProperties eventuateKafkaConfigurationProperties,
                                                           EventuateKafkaProducer eventuateKafkaProducer) {

    return new DatabaseOffsetKafkaStore(eventuateConfigurationProperties.getDbHistoryTopicName(),
            eventuateConfigurationProperties.getMySqlBinLogClientName(),
            eventuateKafkaProducer,
            eventuateKafkaConfigurationProperties);
  }

  @Bean
  public CdcKafkaPublisher<MessageWithDestination> cdcKafkaPublisher(EventuateKafkaConfigurationProperties eventuateKafkaConfigurationProperties,
                                                             DatabaseOffsetKafkaStore databaseOffsetKafkaStore,
                                                             PublishingStrategy<MessageWithDestination> publishingStrategy) {

    return new DbLogBasedCdcKafkaPublisher<>(databaseOffsetKafkaStore,
            eventuateKafkaConfigurationProperties.getBootstrapServers(),
            publishingStrategy);
  }

  @Bean
  public CdcProcessor<MessageWithDestination> cdcProcessor(DbLogClient<MessageWithDestination> dbLogClient,
                                                   DatabaseOffsetKafkaStore databaseOffsetKafkaStore) {

    return new DbLogBasedCdcProcessor<>(dbLogClient, databaseOffsetKafkaStore);
  }
}
