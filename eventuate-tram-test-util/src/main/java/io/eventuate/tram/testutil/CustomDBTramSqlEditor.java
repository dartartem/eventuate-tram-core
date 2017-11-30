package io.eventuate.tram.testutil;

import io.eventuate.local.testutil.CustomDBCreator;
import io.eventuate.local.testutil.SqlScriptEditor;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomDBTramSqlEditor implements SqlScriptEditor {

  private CustomDBCreator customDBCreator;

  public CustomDBTramSqlEditor(CustomDBCreator customDBCreator) {
    this.customDBCreator = customDBCreator;
  }

  @Override
  public List<String> edit(List<String> sqlList) {
    List<String> newList = new ArrayList<>();

    newList.add("CREATE DATABASE IF NOT EXISTS custom");
    newList.add("GRANT ALL PRIVILEGES ON custom.* TO 'mysqluser'@'%' WITH GRANT OPTION");

    sqlList.set(0, sqlList.get(0).replace("eventuate", "custom"));
    newList.addAll(sqlList);

    newList.addAll(customDBCreator.loadSqlScriptAsListOfLines("../mysql/custom-db-part.sql"));

    return newList.stream().filter(s -> !StringUtils.isBlank(s)).collect(Collectors.toList());
  }
}
