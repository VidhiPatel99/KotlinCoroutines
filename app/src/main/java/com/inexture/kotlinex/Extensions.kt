package com.inexture.kotlinex

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.experimental.CoroutineContext


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
fun <T> Call<T>.enqueueDeferredResponse(lifeCycleOwner: LifecycleOwner? = null, callback: Callback<T>? = null): CompletableDeferred<Response<T>> {

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

fun AppCompatActivity.launchCoroutines(coroutineContext: CoroutineContext, launchBody: suspend () -> Unit): Job {
    val job = launch(coroutineContext) {
        launchBody()
    }
    this.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun cancel() {
            println("exiting")
            job.cancel()
        }
    })

    return job
}
