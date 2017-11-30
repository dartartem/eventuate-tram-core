package io.eventuate.tram.testutil;

import io.eventuate.local.testutil.CustomDBCreator;
import io.eventuate.local.testutil.SqlScriptEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomDBTramSqlEditorConfiguration {

  @Autowired
  private CustomDBCreator customDBCreator;

  @Bean
  public SqlScriptEditor sqlScriptEditor() {
    return new CustomDBTramSqlEditor(customDBCreator);
  }
}
