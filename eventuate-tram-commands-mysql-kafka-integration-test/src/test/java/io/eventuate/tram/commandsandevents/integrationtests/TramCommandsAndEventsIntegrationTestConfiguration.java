package io.eventuate.tram.commandsandevents.integrationtests;

import io.eventuate.local.testutil.SqlScriptEditor;
import io.eventuate.tram.commands.common.ChannelMapping;
import io.eventuate.tram.commands.common.DefaultChannelMapping;
import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.consumer.kafka.TramConsumerKafkaConfiguration;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import io.eventuate.tram.testutil.CustomDBSqlEditor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.spy;

@Configuration
@EnableAutoConfiguration
@Import({TramConsumerKafkaConfiguration.class,
        TramMessageProducerJdbcConfiguration.class,
        TramCommandProducerConfiguration.class
})
public class TramCommandsAndEventsIntegrationTestConfiguration {


  @Bean
  public TramCommandsAndEventsIntegrationData tramCommandsAndEventsIntegrationData() {
    return new TramCommandsAndEventsIntegrationData();
}


  @Bean
  public ChannelMapping channelMapping(TramCommandsAndEventsIntegrationData data) {
    return DefaultChannelMapping.builder()
            .with("ReplyTo", data.getAggregateDestination())
            .with("customerService", data.getCommandChannel())
            .build();
  }

  @Bean
  public CommandDispatcher consumerCommandDispatcher(MyTestCommandHandler target) {

    return new CommandDispatcher("customerCommandDispatcher", target.defineCommandHandlers());
  }


  @Bean
  public MyTestCommandHandler myTestCommandHandler() {
    return spy(new MyTestCommandHandler());
  }


  @Bean
  public MyReplyConsumer myReplyConsumer(MessageConsumer messageConsumer) {
    return new MyReplyConsumer(messageConsumer, "ReplyTo");
  }

  @Bean
  @Primary
  public SqlScriptEditor customDBSqlEditor() {
    return new CustomDBSqlEditor();
  }

//  @Bean
//  public DomainEventDispatcher domainEventDispatcher(TramCommandsAndEventsIntegrationData data, MyEventHandler myEventHandler, MessageConsumer messageConsumer) {
//    return new DomainEventDispatcher(data.getEventDispatcherId(),
//            Collections.singletonMap(data.getAggregateDestination(), singleton("*")),
//            myEventHandler,
//            messageConsumer);
//  }
//
//  @Bean
//  public MyEventHandler myEventHandler() {
//    return new MyEventHandler();
//  }
}
