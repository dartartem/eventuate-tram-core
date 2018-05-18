package io.eventuate.tram.cdc.status.service;

import com.google.common.collect.ImmutableMap;
import io.eventuate.local.common.status.CDCStatus;
import io.eventuate.local.common.status.StatusData;
import io.eventuate.tram.messaging.common.MessageImpl;
import io.eventuate.tram.messaging.producer.MessageProducer;
import io.eventuate.tram.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import io.eventuate.util.test.async.Eventually;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TramMessageProducerJdbcConfiguration.class, StatusServiceTest.Config.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StatusServiceTest {

  @Configuration
  @EnableAutoConfiguration
  public static class Config {
  }

  @Value("${test.server.port}")
  private int port;

  @Autowired
  private MessageProducer messageProducer;

  @Test
  public void testStatusRequest() {
    StatusData statusDTO = request();

    Assert.assertEquals(CDCStatus.STARTED, statusDTO.getStatus());
    Assert.assertEquals(0L, statusDTO.getProcessedEvents());
    Assert.assertEquals(Collections.emptyList(), statusDTO.getLastProcessedEvents());

    messageProducer.send("destination123", new MessageImpl("payload123", new HashMap<>(ImmutableMap.of("ID", "id123"))));

    Eventually.eventually(() -> {
      StatusData status = request();

      Assert.assertEquals(CDCStatus.STARTED, status.getStatus());
      Assert.assertEquals(1L, status.getProcessedEvents());
      Assert.assertEquals(1, status.getLastProcessedEvents().size());
      Assert.assertTrue(status.getLastProcessedEvents().get(0).contains("destination123"));
      Assert.assertTrue(status.getLastProcessedEvents().get(0).contains("payload123"));
    });
  }

  private StatusData request() {
    return given()
            .contentType("application/json")
            .when()
            .get(String.format("http://localhost:%s/status", port))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(StatusData.class);
  }
}
