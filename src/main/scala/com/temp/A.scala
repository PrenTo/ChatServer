//package com.temp
//
//import java.net.{ServerSocket, Socket}
//import java.io._
//import java.util.concurrent.{ConcurrentLinkedQueue, ExecutorService, Executors}
//
//case class Message(msg: String, sender: String)
//
//class OutputMessageManager(clients: ConcurrentLinkedQueue[Client], messages: ConcurrentLinkedQueue[Message]) extends Runnable {
//  override def run(): Unit = {
//    if (messages.size() > 0) {
//      val message = messages.poll()
//      outputStreams.parallelStream().forEach(client => {
//        val outputStream = client
//        if (message.sender != outputStream) {
//          outputStream.write(s">> ${message.msg}\n".getBytes)
//        } else {
//          ()
//        }
//      })
//    } else {
//      Thread.sleep(20)
//    }
//    run()
//  }
//}
//
////class ClientConnectionManager(messages: ConcurrentLinkedQueue[Message], clients: ConcurrentLinkedQueue[Client])
//
//object Server extends App {
//  def server(): Unit = {
//    val serverSocket = new ServerSocket(8888)
//    val clients = new ConcurrentLinkedQueue[Client]()
//    val messages = new ConcurrentLinkedQueue[Message]()
//    val outputStreams = new ConcurrentLinkedQueue[OutputStream]()
//    val messageManager = new OutputMessageManager(outputStreams, messages)
//    val pool = Executors.newCachedThreadPool()
//    pool.submit(messageManager)
//    pool.submit(new ConnectionHandler(serverSocket, outputStreams, messages, clients, pool))
//    while (true) {
//      Thread.sleep(20)
//    }
//  }
//
//  server()
//}
//
///**
// *  Connection Handler. Waits for new connections.
// */
//class ConnectionHandler(
//  serverSocket: ServerSocket,
//  outputStreams: ConcurrentLinkedQueue[OutputStream],
//  messages: ConcurrentLinkedQueue[Message],
//  clients: ConcurrentLinkedQueue[Client],
//  pool: ExecutorService
//) extends Runnable {
//
//  override def run() {
//    println("handler")
//    println(Thread.currentThread())
//    val newClient = new Client(serverSocket.accept(), outputStreams, messages)
//    println("New connection, yay!")
//    clients.add(newClient)
//    pool.submit(newClient)
//    run()
//  }
//}
//
///**
// *  Simple Client. Creates an input, output streams and blocks while it waits for client input
// */
//class Client(
//  socket: Socket,
//  name: String,
//  clients: ConcurrentLinkedQueue[Client],
//  messages: ConcurrentLinkedQueue[Message]
//) extends Runnable {
//  val output = socket.getOutputStream
//  outputStreams.add(output)
//  val input = socket.getInputStream
//  val reader = new BufferedReader(new InputStreamReader(input))
//  override def run() {
//    println("client")
//    println(Thread.currentThread())
//    val inputLine = reader.readLine()
//    messages.add(Message(inputLine, output))
//    run()
//  }
//}
//
