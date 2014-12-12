package client

import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_10
import org.java_websocket.handshake.ServerHandshake

class StreamSourceWS extends WebSocketClient {

    StreamSourceWS(URI serverURI) {
        super(serverURI)
    }

    @Override
    void onOpen(ServerHandshake handshakedata) {
        println("You are now connected to: $uri")
    }

    @Override
    void onMessage(String message) {
        //println("Received Message: $message")
    }

    @Override
    void onClose(int code, String reason, boolean remote) {
        println("You have been disconnected from: $uri, Code: $code, Reason: $reason")
    }

    public void onCloseInitiated( int code, String reason ) {
        println("closeInitiated with cod:$code, reason:$reason")
    }

    @Override
    public void onWebsocketCloseInitiated( WebSocket conn, int code, String reason ) {
        onCloseInitiated( code, "websocketclose" )
    }

    @Override
    public void onWebsocketClosing( WebSocket conn, int code, String reason, boolean remote ) {
        onClosing( code, reason, remote );
    }

    public void onClosing( int code, String reason, boolean remote ) {
        println("onClosing with cod:$code, reason:$reason, remote:$remote")
    }
    @Override
    void onError(Exception ex) {
        println("Exception occured $ex.message")
        ex.printStackTrace()
    }
}
