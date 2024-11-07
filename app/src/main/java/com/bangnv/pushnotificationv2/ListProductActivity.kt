package com.bangnv.pushnotificationv2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangnv.pushnotificationv2.databinding.ActivityListProductBinding

class ListProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBinding()
        setupClickListener()
    }

    private fun initializeBinding() {
        binding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupClickListener() {
        binding.btnGoToDetail.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }
}