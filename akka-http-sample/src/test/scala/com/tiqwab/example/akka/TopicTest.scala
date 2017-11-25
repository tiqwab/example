package com.tiqwab.example.akka

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.FunSuiteLike

class TopicTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike with ImplicitSender with ActorSystemRule {

  test("saves messages") {
    val topic = system.actorOf(Topic.props("topic1"))
    topic ! Topic.SaveMessage("hoge", 1L)
    topic ! Topic.SaveMessage("fuga", 2L)
    expectMsg(Message("1", "hoge", 1L))
    expectMsg(Message("2", "fuga", 2L))
  }

  test("returns a message when getting with id") {
    val topic = system.actorOf(Topic.props("topic1"))
    topic ! Topic.SaveMessage("hoge", 1L)
    expectMsgType[Message]
    topic ! Topic.GetMessage("1")
    expectMsg(Some(Message("1", "hoge", 1L)))
  }

  test("returns nothing when getting with unknown id") {
    val topic = system.actorOf(Topic.props("topic1"))
    topic ! Topic.SaveMessage("hoge", 1L)
    expectMsgType[Message]
    topic ! Topic.GetMessage("2")
    expectMsg(None)
  }

  test("returns list of messages") {
    val topic = system.actorOf(Topic.props("topic1"))
    topic ! Topic.SaveMessage("hoge", 1L)
    expectMsgType[Message]
    topic ! Topic.SaveMessage("fuga", 2L)
    expectMsgType[Message]
    topic ! Topic.ListMessage
    expectMsg(Some(Seq(Message("1", "hoge", 1L), Message("2", "fuga", 2L))))
  }

}
