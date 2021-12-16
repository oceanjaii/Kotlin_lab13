package com.example.lab13

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service() {
    private var channel = ""
    private lateinit var thread: Thread
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let {
            channel = it.getString("channel", "")
        }
        broadcast(
            when(channel) {
                "music" -> "welcome to music channel"
                "news" -> "welcome to news channel"
                "sport" -> "welcome to sport channel"
                else -> "wrong channel"
            }
        )

        if (::thread.isInitialized && thread.isAlive)
            thread.interrupt()
        thread = Thread {
            GlobalScope.launch(Dispatchers.Main) {
                delay(3000)
                broadcast(
                    when(channel) {
                        "music" -> "playing monthly to 10 music"
                        "news" -> "playing news"
                        "sport" -> "playing weekly NBA event"
                        else -> "wrong channel"
                    }
                )
            }

           /* try {
                Thread.sleep(3000)
                broadcast(
                    when(channel) {
                        "music" -> "playing monthly to 10 music"
                        "news" -> "playing news"
                        "sport" -> "playing weekly NBA event"
                        else -> "wrong channel"
                    }
                )
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } */
        }
        thread.start()
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? = null

    private fun broadcast(msg: String) =
        sendBroadcast(Intent(channel).putExtra("msg", msg))
}