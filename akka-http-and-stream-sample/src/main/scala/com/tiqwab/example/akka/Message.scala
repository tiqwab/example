package com.tiqwab.example.akka

case class Message(
  id: String,
  body: String,
  timestampMillis: Long
)
