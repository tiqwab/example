package com.tiqwab.example.messaging

import redis.clients.jedis.Jedis

object SampleJedisBasic {

  def main(args: Array[String]): Unit = {
    withRedis { jedis =>
      println(jedis.set("test1", "001"))
      println(jedis.get("test1"))
    }
  }

  def withRedis[T](f: Jedis => T): T = {
    val jedis = new Jedis("localhost")
    try {
      f(jedis)
    } finally {
      jedis.close()
    }
  }

}
