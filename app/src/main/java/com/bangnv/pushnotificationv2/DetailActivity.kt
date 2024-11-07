package com.bangnv.pushnotificationv2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangnv.pushnotificationv2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBinding()
    }

    private fun initializeBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}