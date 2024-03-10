package com.example.seeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seeon.databinding.ContentSplasshhBinding

class SplasshhActivity : AppCompatActivity() {
    private lateinit var binding: ContentSplasshhBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentSplasshhBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}