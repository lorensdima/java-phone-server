package com.example.phoneserver;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AudioWebSocketServer extends WebSocketServer {
    private final List<WebSocket> clients = new CopyOnWriteArrayList<>();

    public AudioWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("Client connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Client disconnected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.i("WS-Server", "Received text message from " + conn.getRemoteSocketAddress() + ": " + message);

        // Relay audio to everyone except the sender
        for (WebSocket client : clients) {
            if (client != conn && client.isOpen()) {
                client.send(message);
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        Log.i("WS-Server", "Received binary message (" + message.remaining() + " bytes) from " + conn.getRemoteSocketAddress());

        // Relay binary data to other clients
        for (WebSocket client : clients) {
            if (client != conn && client.isOpen()) {
                client.send(message);
            }
        }
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started on " + getAddress());
    }
}
