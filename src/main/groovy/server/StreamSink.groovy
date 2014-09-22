package server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

class StreamSink extends WebSocketServer {
    public StreamSink(String host, int port) {
        super(new InetSocketAddress(host, port))
    }

    @Override
    void onOpen(WebSocket conn, ClientHandshake handshake) {
        println("New Connection Received: ${handshake.getResourceDescriptor()}")
        println("From: ${conn.remoteSocketAddress.address.hostAddress}...")
    }

    @Override
    void onClose(WebSocket conn, int code, String reason, boolean remote) {
        println("Closed Connection: $conn, Code: $code, Reason: $reason, Remote: $remote")
    }

    @Override
    void onMessage(WebSocket conn, String message) {
        conn.send("Server ACKing Content: $message")
        println("Received Message : $message")
    }

    @Override
    void onError(WebSocket conn, Exception ex) {
        println("Error from client: $conn, Error: $ex.message")
        ex.printStackTrace()
    }

}
