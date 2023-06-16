package com.example.firebaserealtimeak.activity

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaserealtimeak.R
import com.example.firebaserealtimeak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.firebaseBtn.setOnClickListener{
            val intent = Intent(this, FirebaseActivity::class.java)
            startActivity(intent)
        }

        binding.imageBtn.setOnClickListener {
            val intent = Intent(this,ImageUploadActivity::class.java)
            startActivity(intent)
        }
    }
}
