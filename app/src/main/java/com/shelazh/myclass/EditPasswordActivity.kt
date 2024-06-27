package com.shelazh.myclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shelazh.myclass.databinding.ActivityEditPasswordBinding

class EditPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}