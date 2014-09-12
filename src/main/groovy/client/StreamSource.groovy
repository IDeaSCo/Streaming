package client

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake

class StreamSource extends WebSocketClient {
    StreamSource(URI serverURI) {
        super(serverURI)
    }

    @Override
    void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    void onMessage(String message) {

    }

    @Override
    void onClose(int code, String reason, boolean remote) {

    }

    @Override
    void onError(Exception ex) {

    }
}
