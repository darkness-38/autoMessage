package com.hamza.automessage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnGoToSchedule = findViewById<Button>(R.id.btnGoToSchedule)
        val btnGoToList = findViewById<Button>(R.id.btnGoToList)

        btnGoToSchedule.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnGoToList.setOnClickListener {
            startActivity(Intent(this, ScheduledMessagesActivity::class.java))
        }
    }
}
