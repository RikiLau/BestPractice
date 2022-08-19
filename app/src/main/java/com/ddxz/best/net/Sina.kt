package com.ddxz.best.net
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sina(
    val code: Int? = null, // 200
    val msg: String? = null, // success
    val newslist: List<Newslist?>? = null
)

@JsonClass(generateAdapter = true)
data class Newslist(
    val hottag: String? = null, // 新
    val hotword: String? = null, // 偷偷藏不住网传演员阵容
    val hotwordnum: String? = null // 剧集 2515366
)