package server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

class StreamSinkWS extends WebSocketServer {
    def startTime = [:]
    def endTime  = [:]
    def printedResult = false
    def numberOfClients;

    public StreamSinkWS( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) )
    }

    public StreamSinkWS(String host, int port) {
        super(new InetSocketAddress(host, port))
    }
     def
    @Override
    void onOpen(WebSocket conn, ClientHandshake handshake) {

        println("New Connection Received From: ${conn.remoteSocketAddress.address.hostAddress}...")
    }

    @Override
    void onClose(WebSocket conn, int code, String reason, boolean remote) {
        //println("Closed Connection: $conn, Code: $code, Reason: $reason, Remote: $remote")
    }

    @Override
    void onMessage(WebSocket conn, String message) {
        conn.send("Server ACKing Content: $message")
       /* if(message.contains("Count:0")){
            printedResult = false
            numberOfClients = Character.getNumericValue(message.charAt(0))
            def clientName = message.substring(message.indexOf("SourceClient"),message.indexOf("SourceClient")+14 )
                startTime.put(clientName, System.currentTimeMillis())
        }*/
         if (message.contains("Total") ){
           /* def clientName = message.substring(message.indexOf("SourceClient"),message.indexOf("SourceClient")+14 )
            endTime.put(clientName, System.currentTimeMillis())
            if(startTime.size() == endTime.size() && printedResult == false && endTime.size() == numberOfClients){
                for(key in startTime.keySet()) {
                    println("Time taken by $key : ${(endTime[key] - startTime[key]) / 1000} sec.")
                }
                printedResult = true
                startTime = [:]
                endTime = [:]
            }*/
            println(message)
        }
    }

    @Override
    void onError(WebSocket conn, Exception ex) {
        println("Error from client: $conn, Error: $ex.message")
        ex.printStackTrace()
    }



}
