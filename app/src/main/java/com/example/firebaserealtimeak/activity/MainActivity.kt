package com.example.firebaserealtimeak.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.firebaserealtimeak.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseBtn = findViewById<Button>(R.id.firebaseBtn)

        firebaseBtn.setOnClickListener{
            val intent = Intent(this, FirebaseActivity::class.java)
            startActivity(intent)
        }
    }
}