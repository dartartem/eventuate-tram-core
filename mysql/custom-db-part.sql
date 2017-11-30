DROP Table IF Exists aggregate_instance_subscriptions;
DROP Table IF Exists saga_instance;

CREATE TABLE aggregate_instance_subscriptions(
  aggregate_type VARCHAR(200) DEFAULT NULL,
  aggregate_id VARCHAR(1000) NOT NULL,
  event_type VARCHAR(200) NOT NULL,
  saga_id VARCHAR(1000) NOT NULL,
  saga_type VARCHAR(200) NOT NULL,
  PRIMARY KEY(aggregate_id, event_type, saga_id, saga_type)
);

CREATE TABLE saga_instance(
  saga_type VARCHAR(100) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  state_name VARCHAR(100) NOT NULL,
  last_request_id VARCHAR(100),
  saga_data_type VARCHAR(1000) NOT NULL,
  saga_data_json VARCHAR(1000) NOT NULL,
  PRIMARY KEY(saga_type, saga_id)
);