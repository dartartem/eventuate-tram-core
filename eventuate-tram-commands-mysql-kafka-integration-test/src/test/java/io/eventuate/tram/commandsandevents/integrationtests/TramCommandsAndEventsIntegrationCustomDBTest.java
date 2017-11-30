package io.eventuate.tram.commandsandevents.integrationtests;

import io.eventuate.local.testutil.CustomDBCreator;
import io.eventuate.local.testutil.CustomDBTestConfiguration;
import io.eventuate.local.testutil.SqlScriptEditor;
import io.eventuate.tram.testutil.CustomDBTramSqlEditorConfiguration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomDBTramSqlEditorConfiguration.class, TramCommandsAndEventsIntegrationTestConfiguration.class, CustomDBTestConfiguration.class})
public class TramCommandsAndEventsIntegrationCustomDBTest extends AbstractTramCommandsAndEventsIntegrationTest {

  @Autowired
  private CustomDBCreator customDBCreator;

  @Autowired
  private SqlScriptEditor sqlScriptEditor;

  @Before
  public void createCustomDB() {
    customDBCreator.create(Optional.of(sqlScriptEditor));
  }
}
