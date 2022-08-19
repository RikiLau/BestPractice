package com.ddxz.best.net

import android.util.Log
import com.ddxz.catlibrary.constant.Domain
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.URLDecoder

object ApiService {

    const val TAG = "ApiService"

    val api: Api by lazy {

        Retrofit.Builder()
                .baseUrl(Domain.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor { message ->
                                try {
                                    Log.d(TAG, URLDecoder.decode(message.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25"), "UTF-8"))
                                }
                                catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }.setLevel(HttpLoggingInterceptor.Level.BASIC))
//                        .addInterceptor(ClientHeaderInterceptor())
                        .build())
                .build().create(Api::class.java)
    }
}