package io.eventuate.tram.mysqlkafka.integrationtests;

import io.eventuate.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.local.testutil.SqlScriptEditor;
import io.eventuate.tram.testutil.CustomDBSqlEditor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration
@Import(TramJdbcKafkaConfiguration.class)
public class TramIntegrationTestConfiguration {
  @Bean
  @Primary
  public SqlScriptEditor customDBSqlEditor() {
    return new CustomDBSqlEditor();
  }
}
