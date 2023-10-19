package com.example.g

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Inet4Address

class ServerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)

        val res_text=findViewById<TextView>(R.id.res_server)
        val edit_mes=findViewById<EditText>(R.id.send_server_message)
        val server_send_button=findViewById<Button>(R.id.server_send)
        val server_ip_text=findViewById<TextView>(R.id.server_ip)

        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        val networkCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                server_ip_text.text=linkProperties.linkAddresses.filter {
                    it.address is Inet4Address
                }[0].toString()
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)



        lifecycleScope.launch {
            Control.ServerConnect()
            while (true) {
                Control.ServerReceiveMessage()
                withContext(Dispatchers.Main) {
                    res_text.setText(Control.ser_res_mes)
                }
            }
        }

        server_send_button.setOnClickListener {
            lifecycleScope.launch {
                Control.ServerSendMessage(edit_mes.text.toString())
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Control.ServerDisConnect()
    }
}