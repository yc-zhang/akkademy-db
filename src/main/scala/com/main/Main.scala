package com.main

import akka.actor.{ActorSystem, Props}
import com.akkademy.messages.AkkademyDB

object Main extends App {
  val system = ActorSystem("akkademy")
  system.actorOf(Props[AkkademyDB], name = "akkademy-db")
}
