package com.ddxz.best.net

import java.lang.Exception

data class BaseResult<T>(val status: Int, val data: T? = null, val exception: Exception? = null) {
    companion object {
        const val STATUS_LOADING = 1
        const val STATUS_SUCCESS = 2
        const val STATUS_FAIL = 3
    }
}
