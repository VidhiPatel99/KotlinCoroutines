package com.inexture.kotlinex

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.inexture.kotlinex.RetroFitApi.Post
import com.livinglifetechway.k4kotlin.orFalse
import com.livinglifetechway.k4kotlin_retrofit.RetrofitCallback
import com.livinglifetechway.k4kotlin_retrofit.enqueue
import kotlinx.coroutines.experimental.CancellableContinuation
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.coroutines.experimental.bg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 *
 */
suspend fun <T> Call<T>.enqueueAwait(lifeCycleOwner: LifecycleOwner? = null, callback: Callback<T>? = null): T {

    val deferred = CompletableDeferred<T>()

    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            this.cancel()
        }
    }

    lifeCycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun cancelCalls() {
            this@enqueueAwait.cancel()
        }
    })

    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            callback?.onFailure(call, t)
            deferred.completeExceptionally(t!!)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            callback?.onResponse(call, response)
            deferred.complete(response?.body()!!)
        }

    })

    return deferred.await()
}


/**
 *
 */
fun <T> Call<T>.enqueueDeferred(lifeCycleOwner: LifecycleOwner? = null, callback: Callback<T>? = null): CompletableDeferred<T> {

    val deferred = CompletableDeferred<T>()

    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            this.cancel()
        }
    }

    lifeCycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun cancelCalls() {
            this@enqueueDeferred.cancel()
        }
    })

    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            callback?.onFailure(call, t)
            deferred.completeExceptionally(t!!)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            callback?.onResponse(call, response)
            deferred.complete(response?.body()!!)
        }

    })

    return deferred
}


/**
 *
 */
suspend fun <T> Call<T>.enqueueDeferredResponse(lifeCycleOwner: LifecycleOwner? = null, callback: Callback<T>? = null): CompletableDeferred<Response<T>> {

    val deferred = CompletableDeferred<Response<T>>()

    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            this.cancel()
        }
    }

    lifeCycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun cancelCalls() {
            this@enqueueDeferredResponse.cancel()
        }
    })

    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            callback?.onFailure(call, t)
            deferred.completeExceptionally(t!!)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            callback?.onResponse(call, response)
            deferred.complete(response!!)
        }

    })

    return deferred
}



