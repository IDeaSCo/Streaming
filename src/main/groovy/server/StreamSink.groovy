package server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

class StreamSink extends WebSocketServer {
    public StreamSink(int port) {
        super(new InetSocketAddress(port))
    }

    public StreamSink(InetSocketAddress address) {
        super(address)
    }

    @Override
    void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    void onMessage(WebSocket conn, String message) {

    }

    @Override
    void onError(WebSocket conn, Exception ex) {

    }
}
