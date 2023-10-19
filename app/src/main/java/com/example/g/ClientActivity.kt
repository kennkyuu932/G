package com.example.g

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val server_ip=intent.getStringExtra(MainActivity.server_ip_intent)

        val res_text=findViewById<TextView>(R.id.res_client)
        val edit_mes=findViewById<EditText>(R.id.send_client_message)
        val client_send_button=findViewById<Button>(R.id.client_send)




        lifecycleScope.launch {
            if (server_ip != null) {
                Control.ClientConnect(server_ip)
                while (true) {
                    Control.ClientReceiveMessage()
                    withContext(Dispatchers.Main) {
                        res_text.setText(Control.cli_res_mes)
                    }
                }
            }
        }
        client_send_button.setOnClickListener {
            lifecycleScope.launch {
                Control.ClientSendMessage(edit_mes.text.toString())
            }
        }


    }
}