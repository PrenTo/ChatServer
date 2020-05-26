////#full-example
//import java.net.InetSocketAddress
//
//import akka.actor.{ActorSystem, Props, Actor}
//import akka.io.{Tcp, IO}
//import akka.io.Tcp._
//import akka.util.ByteString
//
//class TCPConnectionManager(address: String, port: Int) extends Actor {
//  import context.system
//  IO(Tcp) ! Bind(self, new InetSocketAddress(address, port))
//
//  override def receive: Receive = {
//    case Bound(local) =>
//      println(s"Server started on $local")
//    case Connected(remote, local) =>
//      val handler = context.actorOf(Props[TCPConnectionHandler])
//      println(s"New connnection: $local -> $remote")
//      sender() ! Register(handler)
//  }
//}
//
//case class Message(msg: String)
//
//class TCPConnectionHandler extends Actor {
//  import context.system
//  import akka.routing.Broadcast
//  override def receive: Actor.Receive = {
//    case Received(data) =>
//      val decoded = data.utf8String
//      val msg = Write(ByteString(s"Message: $decoded"))
////      sender() ! msg
//      println("message")
//      println(self.path)
//      val actors = system.actorSelection("/default/user/parent/*")
//      actors ! Message(decoded)
////      println(actors)
////      system.actorSelection("**/*") ! msg
////      handler() ! Broadcast("Watch out for Davy Jones' locker")
//    case message: ConnectionClosed =>
//      println("Connection has been closed")
//      context stop self
//
//    case Message(decoded) =>
//  }
//}
//
//object Server extends App {
//  val system = ActorSystem()
//  val tcpserver = system.actorOf(Props(classOf[TCPConnectionManager], "localhost", 8080), "parent")
//}
////
//////object AkkaQuickstart extends App {
//////  println("hello world")
//////}
////////#main-class
////////#full-example
//
////import java.net.InetSocketAddress
////
////import akka.actor.{Actor, ActorRef, ActorSystem, Props}
////import akka.io.Tcp._
////import akka.io.{IO, Tcp}
////import akka.util.ByteString
////import scala.concurrent.duration._
////import scala.collection.mutable.ListBuffer
////
////import scala.concurrent.ExecutionContext.Implicits.global
////
////object Main {
////
////  implicit val actorSystem = ActorSystem("actor-system")
////
////  case class Subscribe(i: ActorRef)
////  case class BroadcastMessage(msg: String)
////
////  class Router extends Actor {
////    var connections = new ListBuffer[ActorRef]()
////
////    def receive = {
////      case Subscribe(i) => {
////        println("Subscribed a client")
////        connections.append(i)
////      }
////      case BroadcastMessage(msg) => connections.foreach(c => c ! Write(ByteString(msg)))
////      case Received(data) => {
////        println(s"Received data: ${data.utf8String}")
////        receivedMessage(data, sender())
////      }
////      case PeerClosed => {
////        println("Client disconnected.")
////        connections = connections.filter(_ != sender())
////      }
////    }
////
////    def receivedMessage(message: ByteString, from: ActorRef) = {
////      connections.filter(_ != from).foreach(_ ! Write(ByteString(s"New message: ${message.utf8String}")))
////      from ! Write(ByteString(s"Received: ${message.utf8String}"))
////    }
////  }
////
////  class TcpListener(address: InetSocketAddress) extends Actor {
////
////    IO(Tcp) ! Bind(self, address)
////
////    def receive = {
////      case b @ Bound(localAddress) => {
////        println(s"Server started listening on ${b.localAddress}")
////      }
////      case CommandFailed(_: Bind) => {
////        println("Server stopping.")
////        context stop self
////      }
////      case c @ Connected(remote, local) => {
////        println("New client connected!")
////        val connection = sender()
////        router ! Subscribe(connection)
////        connection ! Register(router)
////      }
////      case Received(data) => {
////        println(s"Received data: $data")
////      }
////    }
////  }
////
////  val router = actorSystem.actorOf(Props[Router])
////
////  def main(args: Array[String]): Unit = {
////    val listener = actorSystem.actorOf(Props(new TcpListener(new InetSocketAddress("localhost", 9999))))
////
//////    actorSystem.scheduler.schedule(60 seconds, 60 seconds) {
//////      router ! BroadcastMessage("Server says: Ping?\n")
//////    }
////  }
////}