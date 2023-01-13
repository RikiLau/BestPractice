package com.ddxz.best

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ddxz.best.constant.LOG_REQUEST
import com.ddxz.best.databinding.ActivityRequestBinding
import com.ddxz.best.net.BaseResult
import com.ddxz.best.view.dialog.LoadingDialogFragment
import kotlinx.coroutines.*

/**
 * 多接口请求时，统一loading弹窗，以及flow的使用
 */
class RequestManagerActivity: AppCompatActivity() {

    companion object {
        const val TAG = "RequestManagerActivity"
    }

    private lateinit var binding: ActivityRequestBinding
    var loadingDialog: LoadingDialogFragment? = null
    val viewModel by viewModels<RequestManagerViewModel>()
    var index1 = 0
    var index2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadingDialog = if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(TAG) as LoadingDialogFragment
        } else {
            LoadingDialogFragment()
        }

        binding.apply {
            tvApi1Req.setOnClickListener {
                viewModel.request1()
            }
            tvApi2Req.setOnClickListener {
                viewModel.requestSina()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loading.collect {
                        Log.d(LOG_REQUEST, "loading $it")
                        if (it) {
                            if (loadingDialog?.isAdded?.not() == true) {
                                loadingDialog?.show(supportFragmentManager, TAG)
                            }
                        } else {
                            loadingDialog?.dismiss()
                        }
                    }
                }
                launch {
                    viewModel.flowResult1.collect {
                        if (it.status == BaseResult.STATUS_SUCCESS) {
                            Log.d(LOG_REQUEST, "请求1 完成 $it ${index1++}")
                        }
                    }
                }
                launch {
                    viewModel.flowResult2.collect {
                        if (it.status == BaseResult.STATUS_SUCCESS) {
                            Log.d(LOG_REQUEST, "请求2 完成 $it ${index2++}")
                        }
                    }
                }
                launch {
                    viewModel.flowResult3.collect {
                        if (it.status == BaseResult.STATUS_SUCCESS) {
                            Log.d(LOG_REQUEST, "请求3 完成 ${it.data?.newslist?.get(0)?.hotword}")
                        }
                    }
                }
            }
        }
    }
}