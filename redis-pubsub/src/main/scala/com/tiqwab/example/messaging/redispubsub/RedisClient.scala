package com.tiqwab.example.messaging.redispubsub

import org.slf4j.LoggerFactory
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig, JedisPubSub}

object RedisClient {

  private val logger = LoggerFactory.getLogger(this.getClass())
  private val host = "localhost"
  private val pool = new JedisPool(new JedisPoolConfig(), host)

  private val subscribers =
    scala.collection.mutable.Map[String, (Thread, Jedis, JedisPubSub)]()

  def publish(message: Message, channel: Channel): Unit = {
    withClient { jedis =>
      jedis.publish(channel.name, message.data)
    }
  }

  def subscribe(channel: Channel, messageReceiver: MessageReceiver): String = {
    val jedis = new Jedis(host)
    val pubsub = createPubSub(messageReceiver)
    val thread = new Thread(() => {
      logger.debug(s"start subscribe to ${channel.name}")
      jedis.subscribe(pubsub, channel.name)
    })
    subscribers.put(thread.getName(), (thread, jedis, pubsub))
    thread.start()
    thread.getName()
  }

  def unsubscribe(name: String): Unit = {
    val (thread, jedis, pubsub) = subscribers(name)
    pubsub.unsubscribe()
    jedis.quit()
  }

  private def withClient[T](f: Jedis => T): T = {
    val jedis = new Jedis(host)
    try {
      f(jedis)
    } finally {
      jedis.quit()
    }
  }

  def createPubSub(messageReceiver: MessageReceiver): JedisPubSub = {
    new JedisPubSub() {
      override def onSubscribe(channel: String,
                               subscribedChannels: Int): Unit =
        logger.debug("onSubscribe")
      override def onUnsubscribe(channel: String,
                                 subscribedChannels: Int): Unit =
        logger.debug("onUnsubscribe")
      override def onPSubscribe(pattern: String,
                                subscribedChannels: Int): Unit =
        logger.debug("onPSubscribe")
      override def onPUnsubscribe(pattern: String,
                                  subscribedChannels: Int): Unit =
        logger.debug("onPUnsubscribe")
      override def onPMessage(pattern: String,
                              channel: String,
                              message: String): Unit =
        logger.debug("onPMessage")
      override def onMessage(channel: String, message: String): Unit =
        messageReceiver.handleMessage(Message(message))
    }
  }

}

trait MessageReceiver {
  def handleMessage(m: Message): Unit
}
