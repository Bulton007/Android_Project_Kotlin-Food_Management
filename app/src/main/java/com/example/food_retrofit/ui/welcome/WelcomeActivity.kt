package com.example.food_retrofit.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_retrofit.R
import com.example.food_retrofit.ui.home.HomePageActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        var btnStarted = findViewById<Button>(R.id.btnStarted)
        btnStarted.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, HomePageActivity::class.java)
            startActivity(intent)
        }
    }
}   