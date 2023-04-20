package com.ddxz.best

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ddxz.best.databinding.ActivityMainBinding
import com.ddxz.best.net.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Register a listener for the 'Show Camera Preview' button.
        binding.tvPermissionRequest.setOnClickListener {
            val intent = Intent(this, PermissionsActivity::class.java)
            startActivity(intent)
        }
        binding.tvCoroutines.setOnClickListener {
            val intent = Intent(this, CoroutinesActivity::class.java)
            startActivity(intent)
        }
        binding.tvRequestManager.setOnClickListener {
            val intent = Intent(this, RequestManagerActivity::class.java)
            startActivity(intent)
        }
        binding.tvFloating.setOnClickListener {
            val intent = Intent(this, RequestManagerActivity::class.java)
            startActivity(intent)
        }
    }
}