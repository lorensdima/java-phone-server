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

class MainActivity : ComponentActivity() {
    private var server: MyHTTPServer? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContent {
            PhoneServerTheme {
                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Your composable content here
                    // For example:
                    // Text("Hello from Compose!")
                }
            }
        }
        try {
            server = MyHTTPServer(8080, this)  // Initialize your server
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Obtain and display the IP address
        val ipAddress = getIPAddress()
//        val ipTextView: TextView = findViewById(R.id.ipAddressTextView)
//        ipTextView.text = "IP Address: $ipAddress"
        Log.d("MainActivity", "IP Address: $ipAddress")
    }

    override fun onDestroy() {
        super.onDestroy()
        server?.stop()  // Stop the server when the activity is destroyed
    }

    private fun getIPAddress(): String {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val connectionInfo: WifiInfo = wifiManager.connectionInfo
        val ipAddressInt = connectionInfo.ipAddress
        return android.text.format.Formatter.formatIpAddress(ipAddressInt)
    }
}
