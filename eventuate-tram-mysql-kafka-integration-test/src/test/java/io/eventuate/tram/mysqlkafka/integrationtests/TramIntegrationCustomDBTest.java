package io.eventuate.tram.mysqlkafka.integrationtests;

import io.eventuate.local.testutil.CustomDBCreator;
import io.eventuate.local.testutil.CustomDBTestConfiguration;
import io.eventuate.local.testutil.SqlScriptEditor;
import io.eventuate.tram.testutil.CustomDBTramSqlEditorConfiguration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomDBTramSqlEditorConfiguration.class, CustomDBTestConfiguration.class, TramIntegrationTestConfiguration.class})
public class TramIntegrationCustomDBTest extends AbstractTramIntegrationTest{

  @Autowired
  private CustomDBCreator customDBCreator;

  @Autowired
  private SqlScriptEditor sqlScriptEditor;

  @Before
  public void createCustomDB() {
    customDBCreator.create(Optional.of(sqlScriptEditor));
  }
}
