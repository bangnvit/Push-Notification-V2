package com.bangnv.pushnotificationv2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bangnv.pushnotificationv2.databinding.ActivityListProductBinding
import com.bangnv.pushnotificationv2.utils.applyWindowInsets

class ListProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initializeBinding()
        applyWindowInsets()
        setupClickListener()
    }

    private fun initializeBinding() {
        binding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun applyWindowInsets() {
        binding.root.applyWindowInsets()
    }

    private fun setupClickListener() {
        binding.btnGoToDetail.setOnClickListener {
            navigateToDetail()
        }
    }

    private fun navigateToDetail() {
        startActivity(Intent(this, DetailActivity::class.java))
    }
}