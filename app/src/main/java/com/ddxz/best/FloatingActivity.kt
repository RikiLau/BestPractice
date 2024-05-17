package com.ddxz.best

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ddxz.best.constant.LOG_REQUEST
import com.ddxz.best.databinding.ActivityFloatingBinding
import com.ddxz.best.databinding.ActivityRequestBinding
import com.ddxz.best.net.BaseResult
import com.ddxz.best.view.dialog.LoadingDialogFragment
import kotlinx.coroutines.*

/**
 * 悬浮窗研究
 */
class FloatingActivity: AppCompatActivity() {

    companion object {
        const val TAG = "FloatingActivity"
    }

    private lateinit var binding: ActivityFloatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFloatingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            tvApi1Req.setOnClickListener {
            }
        }
    }
}