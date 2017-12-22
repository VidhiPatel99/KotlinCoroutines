package com.inexture.kotlinex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.inexture.kotlinex.databinding.ActivityCoroutinesUiBinding
import com.livinglifetechway.k4kotlin.onClick
import com.livinglifetechway.k4kotlin.setBindingView
import com.livinglifetechway.k4kotlin.toast
import com.livinglifetechway.k4kotlin.toastNow
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import org.w3c.dom.Text
import kotlin.coroutines.experimental.EmptyCoroutineContext.get


interface JobHolder {
    val job: Job
}


class CoroutinesUIActivity() : AppCompatActivity(), JobHolder {

    lateinit var mBinging: ActivityCoroutinesUiBinding


    override val job: Job = Job()
    //
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    val View.contextJob: Job
        get() = (context as? JobHolder)?.job ?: NonCancellable

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        mBinging = setBindingView(R.layout.activity_coroutines_ui)

        cancelCoroutines(this, job)
        println("Before launch")
        launch(UI) {
            // <--- Notice this change
            println("Inside coroutine")
            while (true) {
                delay(500)
                println("in loop")// <--- And this is where coroutine suspends
            }
            println("After delay")
        }
        println("After launch")
        setup()

    }


    //blocking operation
    suspend fun fib(x: Int): Int = run(CommonPool) { fibBlocking(x) }

    fun fibBlocking(x: Int): Int = if (x <= 1) 1 else fibBlocking(x - 1) + fibBlocking(x - 2)

    fun setup() {
        var result = "none" // the last result
        // counting animation
        launch(job + UI) {
            var counter = 0
            while (true) {
                mBinging.tvHello.text = "${++counter}: $result"
                delay(100) // update the text every 100ms
                println("launch continue")
            }
        }
        // compute the next fibonacci number of each click
        var x = 1
        mBinging.btnCancel.onClick {
            result = "fib($x) = ${fib(x)}"
            x++
        }
    }

//    fun setup() {
//        mBinging.btnCancel.onClick {
//            // launch coroutine in UI context
//            for (i in 10 downTo 1) { // countdown from 10 to 1
//                mBinging.tvHello.text = "Countdown $i ..." // update text
//                delay(500) // wait half a second
//            }
//            mBinging.tvHello.text = "Done!"
//        }
//
//    }


    //launch coroutine on every button clicks
//    fun View.onClick(action: suspend () -> Unit) {
//        setOnClickListener {
//            launch(UI) {
//                action()
//            }
//        }
//    }

//
//    fun setup() {
//        println("Before launch")
//        launch(UI, CoroutineStart.UNDISPATCHED) {
//            // <--- Notice this change
//            println("Inside coroutine")
//            delay(100)                            // <--- And this is where coroutine suspends
//            println("After delay")
//        }
//        println("After launch")
//    }

    //launch coroutines, cancel current coroutine if possible otherwise discard new
    fun View.onClick(action: suspend () -> Unit) {
        // launch one actor
        val eventActor = actor<Unit>(contextJob + UI, capacity = Channel.UNLIMITED) {
            for (event in channel) action()
        }
        // install a listener to activate this actor
        setOnClickListener {
            eventActor.offer(Unit)
        }
    }

}
