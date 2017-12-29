package io.eventuate.tram.testutil;

import io.eventuate.local.testutil.SqlScriptEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomDBSqlEditor implements SqlScriptEditor {
  @Override
  public List<String> edit(List<String> sqlList) {
    List<String> newSql = new ArrayList<>();
    newSql.add("CREATE DATABASE IF NOT EXISTS custom");
    newSql.add("GRANT ALL PRIVILEGES ON custom.* TO 'mysqluser'@'%' WITH GRANT OPTION");
    newSql.addAll(sqlList);
    newSql.set(2, newSql.get(2).replace("eventuate", "custom"));
    return newSql.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
  }
}
