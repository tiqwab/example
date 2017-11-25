package com.tiqwab.example.akka

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.FunSuiteLike

class TopicListTest
  extends TestKit(ActorSystem("test-system")) with ImplicitSender with FunSuiteLike with ActorSystemRule {

  test("get message") {
    val topicList = system.actorOf(TopicList.props)
    topicList ! TopicList.SaveMessage("topic1", "hoge", 1L)
    expectMsg(Message("1", "hoge", 1L))

    topicList ! TopicList.GetMessage("topic1", "1")
    expectMsg(Some(Message("1", "hoge", 1L)))

    topicList ! TopicList.GetMessage("topic2", "1")
    expectMsg(None)

    topicList ! TopicList.GetMessage("topic1", "2")
    expectMsg(None)
  }

  test("list message") {
    val topicList = system.actorOf(TopicList.props)
    topicList ! TopicList.SaveMessage("topic1", "hoge", 1L)
    expectMsgType[Message]
    topicList ! TopicList.SaveMessage("topic1", "fuga", 2L)
    expectMsgType[Message]
    topicList ! TopicList.SaveMessage("topic2", "foo", 1L)
    expectMsgType[Message]

    topicList ! TopicList.ListMessage("topic1")
    expectMsg(Some(Seq(Message("1", "hoge", 1L), Message("2", "fuga", 2L))))
    topicList ! TopicList.ListMessage("topic2")
    expectMsg(Some(Seq(Message("1", "foo", 1L))))
    topicList ! TopicList.ListMessage("topic3")
    expectMsg(None)
  }

}
