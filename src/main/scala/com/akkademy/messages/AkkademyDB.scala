package com.akkademy.messages

import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable

class AkkademyDB extends Actor {

  val map = new mutable.HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) => {
      log.info(s"received set request - key: $key value: $value")
      map.put(key, value)
    }
    case o => log.info(s"received unknown message: $o")
  }
}
