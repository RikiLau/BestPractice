package com.ddxz.best

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddxz.best.constant.LOG_REQUEST
import com.ddxz.best.net.ApiRepository
import com.ddxz.best.net.BaseResult
import com.ddxz.best.net.Sina
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class RequestManagerViewModel : ViewModel() {

    private val repository = ApiRepository()

    val flowResult1 = MutableStateFlow(BaseResult<Int>(BaseResult.STATUS_SUCCESS))
    val flowResult2 = MutableStateFlow(BaseResult<Int>(BaseResult.STATUS_SUCCESS))
    val flowResult3 = MutableStateFlow(BaseResult<Sina>(BaseResult.STATUS_SUCCESS))

    val loading = combine(flowResult1, flowResult2, flowResult3) { f1, f2, f3 ->
        f1.status == BaseResult.STATUS_LOADING ||
        f2.status == BaseResult.STATUS_LOADING ||
        f3.status == BaseResult.STATUS_LOADING
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    var index1 = 0
    var index2 = 0

    /**
     * 模拟同时请求多个接口（切后台与旋转屏幕）
     */
    fun request1() {
        viewModelScope.launch {
            flowResult1.value = BaseResult(BaseResult.STATUS_LOADING, index1)
            flowResult2.value = BaseResult(BaseResult.STATUS_LOADING, index2)
            delay(2000)
            index1++
            flowResult1.value = BaseResult(BaseResult.STATUS_SUCCESS, index1)
            delay(1000)
            index2++
            flowResult2.value = BaseResult(BaseResult.STATUS_SUCCESS, index2)
        }
    }

    /**
     * 真实请求测试
     */
    fun requestSina() {
        viewModelScope.launch {
            flowResult3.value = BaseResult(BaseResult.STATUS_LOADING, null)
            Log.d(LOG_REQUEST, "requestSina collect1")
            repository.sina.collect {
                Log.d(LOG_REQUEST, "requestSina collect2")
                flowResult3.value = BaseResult(BaseResult.STATUS_SUCCESS, it)
            }
        }
    }
}