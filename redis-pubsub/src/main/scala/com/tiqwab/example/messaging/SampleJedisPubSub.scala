package com.tiqwab.example.messaging

import java.util.concurrent.CountDownLatch

import org.slf4j.LoggerFactory
import redis.clients.jedis.{Jedis, JedisPubSub}

object SampleJedisPubSub extends App {
  val host = "localhost"
  val channel = "channel1"

  // Create subscriber which accepts only 3 messages
  val latch = new CountDownLatch(3)
  val subscriber = new Subscriber(host, channel, latch)

  new Thread(() => subscriber.start()).start()

  // Publish 3 messages with randomized wait time
  new Thread(() => {
    val jedis = new Jedis("localhost")
    List.fill(3)((scala.math.random() * 10000).toLong) foreach {
      waitTimeMillis =>
        Thread.sleep(waitTimeMillis)
        jedis.publish(channel, waitTimeMillis.toString)
    }
  }).start()

  latch.await()
  subscriber.unsubscribe()
}

class Subscriber(val host: String,
                 val channel: String,
                 val latch: CountDownLatch)
    extends JedisPubSub {

  // Assign a new connection since client should not issue commands other than 'subscribe' and 'unsubscribe'
  val jedis = new Jedis(host)
  val logger = LoggerFactory.getLogger(this.getClass)

  def start(): Unit = {
    jedis.subscribe(this, channel)
  }

  override def onUnsubscribe(channel: String, subscribedChannels: Int): Unit = {
    logger.debug(s"unsubscribe $channel")
    jedis.quit()
  }

  override def onSubscribe(channel: String, subscribedChannels: Int): Unit =
    logger.debug(s"subscribe $channel")

  override def onPUnsubscribe(pattern: String, subscribedChannels: Int): Unit =
    logger.debug("onPUnsubscribe")

  override def onPSubscribe(pattern: String, subscribedChannels: Int): Unit =
    logger.debug("onPSubscribe")

  override def onPMessage(pattern: String,
                          channel: String,
                          message: String): Unit =
    logger.debug("onPMessage")

  override def onMessage(channel: String, message: String): Unit = {
    logger.debug(s"get message: $message")
    latch.countDown()
  }

}
