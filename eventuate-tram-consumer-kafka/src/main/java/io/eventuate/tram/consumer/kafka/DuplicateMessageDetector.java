package io.eventuate.tram.consumer.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class DuplicateMessageDetector {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private Optional<String> database;

  public DuplicateMessageDetector(Optional<String> database) {
    this.database = database;
  }

  public boolean isDuplicate(String consumerId, String messageId) {
    try {
      jdbcTemplate.update(String.format("insert into %s(consumer_id, message_id) values(?, ?)", database.map(db -> db + ".").orElse("") + "received_messages"),
              consumerId, messageId);
      return false;
    } catch (DuplicateKeyException e) {
      return true;
    }
  }
}
