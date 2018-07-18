package test.com.akkademy.pong

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.pong.PongActor
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class PongActorSpec extends FunSpecLike with Matchers {

  val system = ActorSystem()

  implicit val timeout: Timeout = Timeout(5 seconds)

  val pongActor: ActorRef = system.actorOf(Props(classOf[PongActor]))

  describe("Pong actor") {
    it ("should respond with Pong") {
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1 second)
      assert(result == "Pong")
    }

    it ("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }


  describe("show some simplify") {
    def askPong(message: String): Future[String] = (pongActor ? message).mapTo[String]

    it ("let's map something") {
      askPong("Ping").map(s => s.length).onSuccess {
        case v => assert(v == 4)
      }

      Thread.sleep(1000)
    }

    it ("let's compose") {
      askPong("Ping")
        .flatMap(v => askPong(s"Ping$v"))
        .recover {
          case e: Throwable => println(s"there was an error: $e")
        }

      val result: Future[String] = for {
        v <- askPong("Ping")
        t <- askPong(s"Ping$v")
      } yield t

      result.recover({ case e: Throwable => println(s"there was an another error: $e") })

      Thread.sleep(1000)
    }
  }
}
