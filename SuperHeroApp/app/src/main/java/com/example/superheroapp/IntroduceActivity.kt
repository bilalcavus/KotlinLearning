package com.example.superheroapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.superheroapp.MySingleton.selectedSuperHero
import com.example.superheroapp.databinding.ActivityIntroduceBinding
import com.example.superheroapp.databinding.ActivityMainBinding

class IntroduceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroduceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroduceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // val intentFromAdapter = intent
        // intentFromAdapter.getSerializableExtra("selectedSuperHero", SuperHero::class.java)
        // val selectedSuperHero = intentFromAdapter.getSerializableExtra("selectedSuperHero") as SuperHero

        val selectedSuperHero = MySingleton.selectedSuperHero
        selectedSuperHero?.let {
            binding.imageView2.setImageResource(selectedSuperHero.image)
            binding.nameTextView.text = selectedSuperHero.name
            binding.jobTextView.text = selectedSuperHero.job
        }


    }
}