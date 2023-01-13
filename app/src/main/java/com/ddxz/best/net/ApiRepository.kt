package com.ddxz.best.net

import android.util.Log
import com.ddxz.best.constant.LOG_REQUEST
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ApiRepository {
    val sina: Flow<Sina> = flow {
        delay(2000)
        val response = ApiService.api.weiboHot()
        emit(response)
    }
}