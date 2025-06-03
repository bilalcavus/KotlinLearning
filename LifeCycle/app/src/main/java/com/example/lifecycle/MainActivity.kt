package com.example.lifecycle

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifecycle.databinding.ActivityMainBinding
import android.content.Intent as Intent1

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        println("onCreate")
    }

    fun nextPage(view: View){
        val intent = Intent(this, SecondActivity::class.java)
        val userInput = binding.editText.text.toString()
        intent.putExtra("isim", userInput)
        startActivity(intent)
        finish()
        // val userInput = binding.editText.text.toString()
        // binding.textView.text = "İsim: $userInput"
    }
}