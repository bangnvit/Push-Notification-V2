package com.bangnv.pushnotificationv2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bangnv.pushnotificationv2.databinding.ActivityDetailBinding
import com.bangnv.pushnotificationv2.utils.applyWindowInsets

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initializeBinding()
        applyWindowInsets()
    }

    private fun initializeBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun applyWindowInsets() {
        binding.root.applyWindowInsets()
    }
}