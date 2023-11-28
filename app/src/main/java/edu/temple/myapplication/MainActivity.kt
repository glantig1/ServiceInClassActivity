package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var timerTV: TextView
    lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false

    val timerHandler = Handler(Looper.getMainLooper()){
        timerTV.text = it.what.toString()
        true
    }
    val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTV = findViewById(R.id.textView)

        bindService(
            Intent(this,TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_pause->{
                Toast.makeText(this,"pause",Toast.LENGTH_SHORT).show()
                if(isConnected) timerBinder.pause()
            }
            R.id.action_start->{
                Toast.makeText(this,"start",Toast.LENGTH_SHORT).show()
                if(isConnected) timerBinder.start(10)
            }
            R.id.action_stop->{
                Toast.makeText(this,"stop",Toast.LENGTH_SHORT).show()
                if(isConnected) timerBinder.stop()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}