package com.example.g

import android.content.Intent
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.g.databinding.ActivityMainBinding
import java.net.Inet4Address

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
//        binding.sampleText.text = stringFromJNI()
        val edit_ip=findViewById<EditText>(R.id.edit_ipaddr)
        val text_ip=findViewById<TextView>(R.id.ip_text)
        val ser_button=findViewById<Button>(R.id.ser_connect)
        val cli_button=findViewById<Button>(R.id.cli_connect)

        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        val networkCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                text_ip.text=linkProperties.linkAddresses.filter {
                    it.address is Inet4Address
                }[0].toString()
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        ser_button.setOnClickListener {
            val intent=Intent(this,ServerActivity::class.java)
            startActivity(intent)
        }
        cli_button.setOnClickListener {
            val intent=Intent(this,ClientActivity::class.java)
            intent.putExtra(server_ip_intent,edit_ip.text.toString())
            startActivity(intent)
        }
    }

    /**
     * A native method that is implemented by the 'g' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'g' library on application startup.
        init {
            System.loadLibrary("g")
        }
        val server_ip_intent="SERVER_IP"
    }
}