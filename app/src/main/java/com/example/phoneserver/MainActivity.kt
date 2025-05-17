package com.example.phoneserver

import android.annotation.SuppressLint
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.phoneserver.ui.theme.PhoneServerTheme
import fi.iki.elonen.NanoHTTPD
import java.io.IOException
import java.net.InetSocketAddress

class MainActivity : ComponentActivity() {

    private lateinit var server: AudioWebSocketServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Optional: setContentView(...) if you want UI/QR-code later

        startServer()
    }

    private fun startServer() {
        val port = 8080
        val addr = InetSocketAddress("0.0.0.0", port)
        server = AudioWebSocketServer(addr).apply { start() }

        printServerEndpoint(port)
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }
}
