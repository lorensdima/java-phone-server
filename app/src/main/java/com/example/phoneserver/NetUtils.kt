package com.example.phoneserver

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import java.net.Inet4Address
import java.net.NetworkInterface

object NetUtils {

    /** Returns a list of IPv4 addresses that are up & not loopback. */
    fun getLocalIPv4(): List<String> =
        NetworkInterface.getNetworkInterfaces()
            .toList()
            .flatMap { it.inetAddresses.toList() }
            .filterIsInstance<Inet4Address>()
            .filter { !it.isLoopbackAddress }
            .map { it.hostAddress ?: "" }
}

/** Call this right after server.start() */
fun Context.printServerEndpoint(port: Int) {
    val addrs = NetUtils.getLocalIPv4()
    addrs.forEach { ip ->
        val msg = "WalkieTalkie server: ws://$ip:$port"
        Log.i("WS-Server", msg)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
