package com.ddxz.best

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.ddxz.best.databinding.ActivityEmptyBinding
import com.ddxz.best.databinding.ActivityRequestBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.lang.NullPointerException

class RequestManagerActivity: AppCompatActivity() {

    companion object {
        const val TAG = "RequestManagerActivity"
    }

    private lateinit var binding: ActivityRequestBinding
    val viewModel by viewModels<RequestManagerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            tvApi1Req.setOnClickListener {
                viewModel.testRequest()
            }
        }

    }
}