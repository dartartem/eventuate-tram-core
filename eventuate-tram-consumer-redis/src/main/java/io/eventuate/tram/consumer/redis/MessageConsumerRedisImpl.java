package io.eventuate.tram.consumer.redis;

import io.eventuate.tram.consumer.common.DuplicateMessageDetector;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.consumer.MessageHandler;
import io.eventuate.tram.messaging.consumer.MessageSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.function.Supplier;

public class MessageConsumerRedisImpl implements MessageConsumer {

  private Logger logger = LoggerFactory.getLogger(getClass());

  public final String consumerId;

  private Supplier<String> subscriptionIdSupplier;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private DuplicateMessageDetector duplicateMessageDetector;

  private String zkUrl;

  private RedisTemplate<String, String> redisTemplate;

  private int partitions;
  private List<Subscription> subscriptions = new ArrayList<>();

  public MessageConsumerRedisImpl(String zkUrl, RedisTemplate<String, String> redisTemplate, int partitions) {
    this(zkUrl, () -> UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            redisTemplate,
            partitions);
  }

  public MessageConsumerRedisImpl(String zkUrl,
                                  Supplier<String> subscriptionIdSupplier,
                                  String consumerId,
                                  RedisTemplate<String, String> redisTemplate,
                                  int partitions) {

    this.zkUrl = zkUrl;
    this.subscriptionIdSupplier = subscriptionIdSupplier;
    this.consumerId = consumerId;
    this.redisTemplate = redisTemplate;
    this.partitions = partitions;

    logger.info("Consumer created (consumer id = {})", consumerId);
  }

  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
  }

  public DuplicateMessageDetector getDuplicateMessageDetector() {
    return duplicateMessageDetector;
  }

  public void setDuplicateMessageDetector(DuplicateMessageDetector duplicateMessageDetector) {
    this.duplicateMessageDetector = duplicateMessageDetector;
  }

  @Override
  public MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler) {

    logger.info("consumer subscribes to channels (consumer id = {}, subscriber id {}, channels = {})", consumerId, subscriberId, channels);

    Subscription subscription = new Subscription(zkUrl,
            subscriptionIdSupplier.get(),
            consumerId,
            redisTemplate,
            transactionTemplate,
            duplicateMessageDetector,
            subscriberId,
            channels,
            handler,
            partitions);

    subscriptions.add(subscription);

    return subscription::close;
  }

  public void setSubscriptionLifecycleHook(SubscriptionLifecycleHook subscriptionLifecycleHook) {
    subscriptions.forEach(subscription -> subscription.setSubscriptionLifecycleHook(subscriptionLifecycleHook));
  }

  public void close() {
    subscriptions.forEach(Subscription::close);
    subscriptions.clear();
  }
}