package com.temp

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props, Actor, ActorRef}
import akka.io.{Tcp, IO}
import akka.io.Tcp._
import akka.util.ByteString

class TCPConnectionManager(address: String, port: Int) extends Actor {
  import context.system
  IO(Tcp) ! Bind(self, new InetSocketAddress(address, port))

  override def receive: Receive = {
    case Bound(local) =>
      println(s"Server started on $local")

    case Connected(remote, local) =>
      val connection = context.actorOf(Props[TCPConnectionHandler])
      println(s"New connnection: $local -> $remote")
      sender() ! Register(connection)
      println(sender())
      println(self.path)
  }

}

class TCPConnectionHandler extends Actor {
  import context.system
  import akka.routing.Broadcast
  override def receive: Actor.Receive = {
    case Received(data) =>
      val decoded = data.utf8String
      val msg = Write(ByteString(s"Message: $decoded"))

      // Was `system.actorSelection("/user/**/*")
      val actors = system.actorSelection("/system/IO-TCP/selectors/**/*")
      actors ! msg
    case message: ConnectionClosed =>
      println("Connection has been closed")
      context stop self
  }
}

object AkkaChatServer extends App {
  val system = ActorSystem("Server")
  val server = system.actorOf(Props(classOf[TCPConnectionManager], "localhost", 8080))
}