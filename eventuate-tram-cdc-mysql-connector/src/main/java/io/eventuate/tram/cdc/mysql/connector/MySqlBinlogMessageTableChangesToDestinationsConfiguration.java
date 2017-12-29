package io.eventuate.tram.cdc.mysql.connector;

import io.eventuate.javaclient.spring.jdbc.EventuateSchema;
import io.eventuate.local.common.EventuateConfigurationProperties;
import io.eventuate.local.common.JdbcUrl;
import io.eventuate.local.common.JdbcUrlParser;
import io.eventuate.local.common.MySqlBinlogCondition;
import io.eventuate.local.db.log.common.DbLogClient;
import io.eventuate.local.mysql.binlog.IWriteRowsEventDataParser;
import io.eventuate.local.mysql.binlog.MySqlBinaryLogClient;
import io.eventuate.local.mysql.binlog.SourceTableNameSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Conditional(MySqlBinlogCondition.class)
public class MySqlBinlogMessageTableChangesToDestinationsConfiguration {

  @Bean
  public SourceTableNameSupplier sourceTableNameSupplier(EventuateConfigurationProperties eventuateConfigurationProperties) {
    return new SourceTableNameSupplier(eventuateConfigurationProperties.getSourceTableName(), MySQLTableConfig.EVENTS_TABLE_NAME);
  }

  @Bean
  public IWriteRowsEventDataParser eventDataParser(EventuateSchema eventuateSchema,
          DataSource dataSource,
          EventuateConfigurationProperties eventuateConfigurationProperties) {
    return new WriteRowsEventDataParser(dataSource, eventuateSchema);
  }

  @Bean
  public DbLogClient<MessageWithDestination> mySqlBinaryLogClient(@Value("${spring.datasource.url}") String dataSourceURL,
                                                 EventuateConfigurationProperties eventuateConfigurationProperties,
                                                 SourceTableNameSupplier sourceTableNameSupplier,
                                                 IWriteRowsEventDataParser<MessageWithDestination> eventDataParser) {

    JdbcUrl jdbcUrl = JdbcUrlParser.parse(dataSourceURL);
    return new MySqlBinaryLogClient<>(eventDataParser,
            eventuateConfigurationProperties.getDbUserName(),
            eventuateConfigurationProperties.getDbPassword(),
            jdbcUrl.getHost(),
            jdbcUrl.getPort(),
            eventuateConfigurationProperties.getBinlogClientId(),
            sourceTableNameSupplier.getSourceTableName(),
            eventuateConfigurationProperties.getMySqlBinLogClientName(),
            eventuateConfigurationProperties.getBinlogConnectionTimeoutInMilliseconds(),
            eventuateConfigurationProperties.getMaxAttemptsForBinlogConnection());
  }
}
