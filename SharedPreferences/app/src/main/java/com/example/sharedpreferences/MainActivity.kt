package com.example.sharedpreferences

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferences
    var receivedName : String? = null
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
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE)

        receivedName = sharedPref.getString("name", "")
        if (receivedName == ""){
            binding.textView.text = "No name found"
        }
        else {
            binding.textView.text = receivedName
        }
    }

    fun save(view: View){
        val userName = binding.editTextText.text.toString()
        if (userName == ""){
            Toast.makeText(this@MainActivity, "please enter name!", Toast.LENGTH_LONG).show()
        }
        else {
            sharedPref.edit().putString("name", userName).apply()
            binding.textView.text = userName
        }
    }

    fun delete(view: View){
        receivedName = sharedPref.getString("name", "")
        if (receivedName != ""){
            sharedPref.edit().remove("name").apply()
        }
        binding.textView.text = null
    }
}