package com.example.seeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seeon.databinding.ContentLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ContentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}