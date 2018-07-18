package com.akkademy.messages

import akka.actor.{Actor, Status}
import akka.event.Logging

import scala.collection.mutable

case class SetRequest (key: String, value: String)
case class GetRequest (key: String)
case class KeyNotFoundException(key: String) extends Exception

class AkkademyDB extends Actor {

  val map = new mutable.HashMap[String, String]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) => {
      log.info(s"received set request - key: $key value: $value")
      map.put(key, value)
      sender() ! Status.Success
    }
    case GetRequest(key) => {
      log.info(s"received get request - key: $key")
      map.get(key) match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(KeyNotFoundException(key))
      }

    }
    case _ => Status.Failure(ClassNotFoundException)
  }
}
