package com.example.beginnercalculator

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.beginnercalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




    }

    fun plus(view: View) {
        val firstNumber = binding.editTextText.text.toString().toDoubleOrNull() ?: 0.0
        val secondNumber = binding.editTextText2.text.toString().toDoubleOrNull() ?: 0.0
        val result = firstNumber + secondNumber
        binding.textView.text = "Result: $result"
    }

    fun minus(view: View) {
        val firstNumber = binding.editTextText.text.toString().toDoubleOrNull() ?: 0.0
        val secondNumber = binding.editTextText2.text.toString().toDoubleOrNull() ?: 0.0
        val result = firstNumber - secondNumber
        binding.textView.text = "Result: $result"
    }
    fun multiply(view: View) {
        val firstNumber = binding.editTextText.text.toString().toDoubleOrNull() ?: 0.0
        val secondNumber = binding.editTextText2.text.toString().toDoubleOrNull() ?: 0.0
        val result = firstNumber * secondNumber
        binding.textView.text = "Result: $result"
    }

    fun divide(view: View) {
        val firstNumber = binding.editTextText.text.toString().toDoubleOrNull() ?: 0.0
        val secondNumber = binding.editTextText2.text.toString().toDoubleOrNull() ?: 0.0
        val result = if (secondNumber!= 0.0){
            firstNumber / secondNumber
        }
        else{
            "Cannot divide by zero"
        }
        binding.textView.text = "Result: $result"
    }
}