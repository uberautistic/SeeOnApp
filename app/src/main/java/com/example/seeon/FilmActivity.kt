package com.example.seeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seeon.databinding.ContentFilmBinding

class FilmActivity : AppCompatActivity() {
    private lateinit var binding: ContentFilmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url= intent.getStringExtra("URL")
        binding.webView.loadUrl(url!!)
    }

    override fun onStart() {
        super.onStart()
        binding.backButton.setOnClickListener {
            this.finish()
        }
    }
}