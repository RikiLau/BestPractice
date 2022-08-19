package com.ddxz.best.net

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    /**
     *微博热搜
     */
    @GET("/weibohot/index?key=1107497adc4bc4fad81ded6b47e2233b")
    suspend fun weiboHot(): Sina?
}