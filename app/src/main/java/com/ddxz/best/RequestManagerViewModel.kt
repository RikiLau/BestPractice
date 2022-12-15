package com.ddxz.best

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddxz.best.constant.LOG_REQUEST
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RequestManagerViewModel : ViewModel() {

    val flowResult1 = flow<Boolean> {
        delay(3000)
        Log.d(LOG_REQUEST, "请求1请求完成")
        emit(true)
    }
    val flowResult2 = flow<Boolean> {
        delay(2000)
        Log.d(LOG_REQUEST, "请求2请求完成")
        emit(true)
    }
    val unbind = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            listOf(flowResult1, flowResult2).merge()
                    .onStart {
                        Log.d(LOG_REQUEST, "showDialog")
                    }.onCompletion {
                        Log.d(LOG_REQUEST, "dismissDialog")
                    }.collect {

                    }
        }
    }

    fun testRequest() {
        Log.d(LOG_REQUEST, "请求1开始请求")
        Log.d(LOG_REQUEST, "请求2开始请求")
        viewModelScope.launch {
            delay(3000)
            flowResult1.
        }
        viewModelScope.launch {
            flowResult2.collect {

            }
        }
    }
}