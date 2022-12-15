package com.ddxz.best

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ddxz.best.databinding.ActivityEmptyBinding
import kotlinx.coroutines.*
import java.lang.NullPointerException

class CoroutinesActivity: AppCompatActivity() {

    companion object {
        const val TAG = "CoroutinesActivity"
    }

    private lateinit var binding: ActivityEmptyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmptyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // async虽然会暴露异常到调用处await，但其协程如果非根协程调用，则会把协程传递到父协程（尽管已try catch await）。
        // 如果是根协程调用，已try catch但并不会在scope handler获取到异常
//        CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "async使用Job()会捕获")
//        }).launch {
//            val asyncA = async(SupervisorJob()) {
//                String()
//            }
//            val asyncB = async(SupervisorJob()) {
//                throw NullPointerException()
//            }
//
//            kotlin.runCatching { asyncA.await() }.onFailure {
//                Log.d(TAG, "并没有异常")
//            }
//            kotlin.runCatching { asyncB.await() }.onFailure {
//                Log.d(TAG, "这里一定能捕获异常")
//            }
//
//        }

//        val scope1 = CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "捕获异常1")
//        })
//        val scope2 = CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "捕获异常2")
//        })
//        val asyncA = scope1.async {
//            throw NullPointerException()
//        }
//        scope2.launch {
//            kotlin.runCatching {
//                asyncA.await()
//            }.onFailure {
//                Log.d(TAG, "捕获异常3")
//            }
//        }

//        lifecycleScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "不加会崩溃")
//        }) {
//            kotlin.runCatching {
//                val result1 = getDates1Async()
//                val result2 = getDates2Async()
//                result1.await()
//                result2.await()
//            }.onFailure {
//                Log.d(TAG, "能捕获，但是会传递给上层")
//            }
//        }

//        lifecycleScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "不加会崩溃")
//        }) {
//            kotlin.runCatching {
//                coroutineScope {
//                    val result1 = getDates1Async()
//                    val result2 = getDates2Async()
//                    result1.await()
//                    result2.await()
//                }
//            }.onFailure {
//                Log.d(TAG, "能捕获，不会传递给上层")
//            }
//        }

        // 用了withContext后，内部启动的协程都能try住
//        lifecycleScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "不加触发")
//        }) {
//            kotlin.runCatching {
//                withContext(Dispatchers.IO) {
//                    launch {
//                        val result1 = getDates1Async()
//                        val result2 = getDates2Async()
//                        result1.await()
//                        result2.await()
//                    }
//                    withContext(Dispatchers.Default) {
//                        launch {
//                            val result1 = getDates1Async()
//                            val result2 = getDates2Async()
//                            result1.await()
//                            result2.await()
//                        }
//                    }
//                }
//                withContext(Dispatchers.Default) {
//                    launch {
//                        val result1 = getDates1Async()
//                        val result2 = getDates2Async()
//                        result1.await()
//                        result2.await()
//                    }
//                }
//            }.onFailure {
//                Log.d(TAG, "捕获了异常")
//            }
//        }

//        lifecycleScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
//            Log.d(TAG, "已try不会触发")
//        }) {
//            supervisorScope {
//                try {
//                    val result1 = getDates1Async()
//                    val result2 = getDates2Async()
//                    result1.await()
//                    result2.await()
//                }
//                catch (e: Exception) {
//                    Log.d(TAG, "能捕获异常，不try会传到父协程CoroutineExceptionHandler")
//                }
//            }
//        }

        // isActive改成true会死循环
//        lifecycleScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
//
//        }) {
//            val job = lifecycleScope.launch {
//                while (isActive) {
//                    kotlin.runCatching {
//                        Log.d(TAG, "协程打印中... ${System.currentTimeMillis()}")
//                        delay(1000)
//                    }.onFailure {
//                        Log.d(TAG, "捕获了 $it")
//                    }
//                }
//            }
//            delay(2300)
//            Log.d(TAG, "协程代码取消了")
//            job.cancelAndJoin()
//        }

        lifecycleScope.launch {
            launch {
                withContext(NonCancellable) {

                }
            }
        }
    }

    private fun CoroutineScope.getDates1Async() = async {
        delay(500)
        Log.d(TAG, "成功获取dates1")
    }

    private fun CoroutineScope.getDates2Async() = async {
        delay(10)
        throw NullPointerException()
    }

    private fun CoroutineScope.getDates3Async() = async {
        delay(1000)
        Log.d(TAG, "成功获取dates31")
    }
}