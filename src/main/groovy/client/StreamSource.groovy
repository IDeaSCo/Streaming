package client

import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_10
import org.java_websocket.handshake.ServerHandshake

class StreamSource extends WebSocketClient {

    StreamSource(URI serverURI) {
        super(serverURI)
    }

    @Override
    void onOpen(ServerHandshake handshakedata) {
        println("You are now connected to: $uri")
    }

    @Override
    void onMessage(String message) {
        println("Received Message: $message")
    }

    @Override
    void onClose(int code, String reason, boolean remote) {
        println("You have been disconnected from: $uri, Code: $code, Reason: $reason")
    }

    @Override
    void onError(Exception ex) {
        println("Exception occured $ex.message")
        ex.printStackTrace()
    }
}
