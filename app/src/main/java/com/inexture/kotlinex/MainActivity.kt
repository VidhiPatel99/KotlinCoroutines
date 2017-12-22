package com.inexture.kotlinex

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.inexture.kotlinex.R.id.info
import com.inexture.kotlinex.RetroFitApi.ApiClient
import com.inexture.kotlinex.RetroFitApi.Post
import com.inexture.kotlinex.RetroFitApi.UserResp
import com.inexture.kotlinex.databinding.ActivityMainBinding
import com.inexture.kotlinex.model.ConsResp
import com.livinglifetechway.k4kotlin.hide
import com.livinglifetechway.k4kotlin.setBindingView
import com.livinglifetechway.k4kotlin.show
import com.livinglifetechway.k4kotlin_retrofit.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.measureTimeMillis

//suspend fun <T> Call<T>.nqueue2(callback: Callback<T>) = enqueueAwait(callback)


class MainActivity() : AppCompatActivity(), LifecycleOwner, JobHolder {
    private val mList: ArrayList<MyListItem>? = arrayListOf()
    private val mEmptyList: ArrayList<MyListItem> = arrayListOf()
    private var msg: String? = null
    private var mConsList: List<ConsResp.Data>? = ArrayList()
    private var newConsList: ArrayList<ConsResp.Data>? = arrayListOf()

    override val job: Job = Job()


    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBindingView(R.layout.activity_main)
        mBinding.rvTest.layoutManager = LinearLayoutManager(this)


        cancelCoroutines(this, job)
//        mBinding.ivAdd.setOnClickListener {
//            mBinding.progressBar.show()
//            async {
//                val userData = getData()
//                println(userData.toString())
//                mBinding.tvNoDataFound.text = "vidhi"
//            }
//        }
        //val postInfo = getData()
        // println(postInfo)


        //get CompletedDeferred<T>
//        var postInfoFromExtention: UserResp? = null
//        launch(UI, CoroutineStart.UNDISPATCHED) {
//            val call = ApiClient.service.getUserDetails()
//            postInfoFromExtention = call.enqueueAwait(callback = RetrofitCallback {
//                progressView = mBinding.progressBar
//                onCompleted { call, response, throwable ->
//                }
//
//                on200Ok { call, response ->
//                    val info = response?.body()
//                    println("onResponse....enqueueDeferred        " + response?.body())
////                    mBinding.tvNoDataFound.text = info?.title
//                }
//
//                onFailureCallback { call, throwable ->
//                    println(throwable)
//                }
//            })
//            val info = postInfoFromExtention
//            println("deferredResp....enqueueDeferred      " + info)
//            mBinding.tvNoDataFound.text = info!!.data[0].first_name
//        }
//
//
//        mBinding.tvTest.text = "after enqueue await"
//
//
//        //directly get response.body()
//        async(UI) {
//            var postInfoFromExtention: UserResp? = null
//            val job = launch(UI) {
//                val call = ApiClient.service.getUserDetails()
//                postInfoFromExtention = call.enqueueAwait(this@MainActivity, RetrofitCallback {
//                    progressView = mBinding.progressBar
//                    onCompleted { call, response, throwable ->
//                    }
//
//                    on200Ok { call, response ->
//                        val info = response?.body()
//                        println("onResponse....enqueueAwait     " + response?.body())
//                    }
//
//                    onFailureCallback { call, throwable ->
//                        println(throwable)
//                    }
//                })
//            }
//            job.join()
//            val info = postInfoFromExtention
//            println("deferredResp....enqueueAwait         " + info)
//            mBinding.tvNoDataFound.text = info!!.data[0].first_name
//
//        }

//
//        //get Deferred<Response<T>>
        async(UI) {
            var postInfoFromExtention: CompletableDeferred<Response<UserResp>>? = null
            launch(UI, CoroutineStart.UNDISPATCHED) {
                val call = ApiClient.service.getUserDetails()
                postInfoFromExtention = call.enqueueDeferredResponse(lifeCycleOwner = null, callback = RetrofitCallback {
                    progressView = mBinding.progressBar
                    onCompleted { call, response, throwable ->
                    }

                    on200Ok { call, response ->
                        val info = response?.body()
                        println("onResponse....enqueueDeferredResponse       " + response?.body())
//                    mBinding.tvNoDataFound.text = info?.title
                    }

                    onFailureCallback { call, throwable ->
                        println(throwable)
                    }
                })
            }
            val info = postInfoFromExtention?.await()
            println("deferredResp....enqueueDeferredResponse       " + info?.body())
            mBinding.tvNoDataFound.text = info?.body()!!.data[0].first_name
            mBinding.tvTest.text = "after deferred call"

        }


        //with extract suspending fun
//        runBlocking {
//            val job = launch {
//                delay(2000)
//                println("World!")
//            }
//            println("Hello,")
//            job.join()
//        }

//        join ex
//        mBinding.ivAdd.setOnClickListener {
//            println("before runblocking " + Thread.currentThread().id)
//            runBlocking {
//                println("in runblocking " + Thread.currentThread().id)
//
//                val job = launch(UI) {
//                    println("In launch " + Thread.currentThread().id)
//                }
//                println("after launch " + Thread.currentThread().id)
//                job.join() // wait until child coroutine completes
//            }
//            println("after runBlocking " + Thread.currentThread().id)
//        }


        //start multiple coroutines
//        runBlocking {
//            val jobs = List(10_000) {
//                // launch a lot of coroutines and list their jobs
//                launch {
//                    delay(1000)
//                    println(it)
//                    delay(5000)
//
//                }
//            }
//            println("scheduled")
//            jobs.forEach {
//                it.join()
//            } // wait for all jobs to complete
//            println("completed")
//
//        }
        //background process with responsive UI
//        runBlocking {
//            launch() {
//                repeat(1000) { i ->
//                    println("I'm sleeping $i ...")
//                    delay(500L)
//                }
//            }
//            delay(1300L) // just quit after delay
//        }


        //cancel a launch(coroutine)
//        runBlocking {
//            val job = launch() {
//                repeat(1000) { i ->
//                    println("I'm sleeping $i ...")
//                    delay(500)
//                }
//            }
//            delay(2000) // delay a bit
//            println("main: I'm tired of waiting!")
////            job.cancel() // cancels the job
////            job.join() // waits for job's completion
//            job.cancelAndJoin()
//            println("main: Now I can quit.")
//        }


        //cancel computation without checking isActive
//        runBlocking {
//            val startTime = System.currentTimeMillis()
//            val job = launch() {
//                var nextPrintTime = startTime
//                var i = 0
//                while (i < 5) { // computation loop, just wastes CPU
//                    // print a message twice a second
//                    if (System.currentTimeMillis() >= nextPrintTime) {
//                        println("I'm sleeping ${i++} ...")
//                        nextPrintTime += 500L
//                    }
//                }
//            }
//            delay(1300L) // delay a bit
//            println("main: I'm tired of waiting!")
//            job.cancel() // cancels the job and waits for its completion
//            job.join()
//            println("main: Now I can quit.")
//        }


        //cancel computation with checking isActive
//        runBlocking<Unit> {
//            val startTime = System.currentTimeMillis()
//            val job = launch {
//                var nextPrintTime = startTime
//                var i = 0
//                while (isActive) { // cancellable computation loop
//                    // print a message twice a second
//                    if (System.currentTimeMillis() >= nextPrintTime) {
//                        println("I'm sleeping ${i++} ...")
//                        nextPrintTime += 500L
//                    }
//                }
//            }
//            delay(1300L) // delay a bit
//            println("main: I'm tired of waiting!")
//            job.cancelAndJoin() // cancels the job and waits for its completion
//            println("main: Now I can quit.")
//        }


        //closing resource with finally
//        runBlocking<Unit> {
//            val job = launch {
//                try {
//                    repeat(1000) { i ->
//                        println("I'm sleeping $i ...")
//                        delay(500L)
//                    }
//                } finally {
//                    println("I'm running finally")
//                }
//            }
//            delay(1300L) // delay a bit
//            println("main: I'm tired of waiting!")
//            job.cancelAndJoin() // cancels the job and waits for its completion
//            println("main: Now I can quit.")
//        }


        //Run non-cancellable block
//        runBlocking<Unit> {
//            val job = launch {
//                try {
//                    repeat(1000) { i ->
//                        println("I'm sleeping $i ...")
//                        delay(500)
//                    }
//                }
//                finally {
//                    run(NonCancellable) {
//                        delay(5000)
//                        println("I'm running finally")
//                        println("And I've just delayed for 5 sec because I'm non-cancellable")
//                    }
//                }
//            }
//            delay(1300) // delay a bit
//            println("main: I'm tired of waiting!")
//            job.cancelAndJoin() // cancels the job and waits for its completion
//            println("main: Now I can quit.")
//        }


        //timeout
//        runBlocking<Unit> {
//            val result = withTimeoutOrNull(1300L) {
//                repeat(1000) { i ->
//                    println("I'm sleeping $i ...")
//                    delay(200L)
//                }
//                "Done" // will get cancelled before it produces this result
//            }
//            println("Result is $result")
//        }


        //composing suspending functions(Sequential by default)
//        runBlocking {
//            val time = measureTimeMillis {
//                val one = doSomethingUsefulOne()
//                val two = doSomethingUsefulTwo()
//                println("The answer is ${one + two}")
//            }
//            println("Completed in $time ms")
//        }


        //composing suspending functions(concurrent by async)
//        runBlocking<Unit> {
//            val time = measureTimeMillis {
//                val one = async { doSomethingUsefulOne() }
//                val two = async { doSomethingUsefulTwo() }
//                println("The answer is ${one.await() + two.await()}")
//            }
//            println("Completed in $time ms")
//        }


        //async start by lazy(take same time as sequential)
//        runBlocking<Unit> {
//            val time = measureTimeMillis {
//                val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
//                val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
//                println("The answer is ${one.await() + two.await()}")
//            }
//            println("Completed in $time ms")
//        }


        //dispatcher and threads
//        runBlocking<Unit> {
//            val jobs = arrayListOf<Job>()
//            jobs += launch(Unconfined) {
//                // not confined -- will work with main thread
//                println("      'Unconfined': I'm working in thread ${Thread.currentThread().name}")
//            }
//            jobs += launch(coroutineContext) {
//                // context of the parent, runBlocking coroutine
//                println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
//            }
//            jobs += launch(CommonPool) {
//                // will get dispatched to ForkJoinPool.commonPool (or equivalent)
//                println("      'CommonPool': I'm working in thread ${Thread.currentThread().name}")
//            }
//            jobs += launch(newSingleThreadContext("MyOwnThread")) {
//                // will get its own new thread
//                println("          'newSTC': I'm working in thread ${Thread.currentThread().name}")
//            }
//            jobs.forEach { it.join() }
//        }


        //Unconfined vs confined dispatcher
//        runBlocking<Unit> {
//            val jobs = arrayListOf<Job>()
//            jobs += launch(Unconfined) {
//                // not confined -- will work with main thread
//                println("      'Unconfined': I'm working in thread ${Thread.currentThread().name}")
//                delay(500)
//                println("      'Unconfined': After delay in thread ${Thread.currentThread().name}")
//            }
//            jobs += launch(coroutineContext) {
//                // context of the parent, runBlocking coroutine
//                println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
//                delay(1000)
//                println("'coroutineContext': After delay in thread ${Thread.currentThread().name}")
//            }
//            jobs.forEach { it.join() }
//        }


        //jumping between threads
//        fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
//
//        newSingleThreadContext("Ctx1").use { ctx1 ->
//            newSingleThreadContext("Ctx2").use { ctx2 ->
//                runBlocking(ctx1) {
//                    log("Started in ctx1")
//                    run(ctx2) {
//                        log("Working in ctx2")
//                    }
//                    log("Back to ctx1")
//                }
//            }
//        }


        //jon in context
//        runBlocking<Unit> {
//            println("My job is ${coroutineContext[Job]}")
//        }

        //combination of parent and child coroutine....parent doesn't wait for complation of child coroutine
//        runBlocking<Unit> {
//            // launch a coroutine to process some kind of incoming request
//            val request = launch {
//                repeat(3) { i ->
//                    // launch a few children jobs
//                    launch(coroutineContext) {
//                        delay((i + 1) * 200L) // variable delay 200ms, 400ms, 600ms
//                        println("Coroutine $i is done")
//                    }
//                }
//                println("request: I'm done and I don't explicitly join my children that are still active")
//            }
//            request.join() // wait for completion of the request, including all its children
//            println("Now processing of the request is complete")
//        }


        //channel
//        runBlocking<Unit> {
//            val channel = Channel<Int>()
//            launch {
//                // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
//                for (x in 1..5) channel.send(x * x)
//            }
//            // here we print five received integers:
//            repeat(5) { println(channel.receive()) }
//            println("Done!")
//        }


        //closing channel by sending closing token
//        runBlocking<Unit> {
//            val channel = Channel<Int>()
//            launch {
//                for (x in 1..5) channel.send(x * x)
//                channel.close() // we're done sending(sending closing token)
//            }
//            // here we print received values using `for` loop (until the channel is closed)
//            for (y in channel) println(y)
//            println("Done!")
//        }


        //building channel producers
//        fun produceSquares() = produce {
//            for (x in 1..5) send(x * x)
//        }
//
//        runBlocking<Unit> {
//            val squares = produceSquares()
//            squares.consumeEach { println(it) }
//            println("Done!")
//        }


        //pipelines
//        fun produceNumbers() = produce<Int> {
//            var x = 1
//            while (true) send(x++) // infinite stream of integers starting from 1
//        }

//        fun square(numbers: ReceiveChannel<Int>) = produce<Int> {
//            for (x in numbers) send(x * x)
//        }
//
//        runBlocking<Unit> {
//            val numbers = produceNumbers() // produces integers from 1 and on
//            val squares = square(numbers) // squares integers
//            for (i in 1..10) println(squares.receive()) // print first five
//            println("Done!") // we are done
//            squares.cancel() // need to cancel these coroutines in a larger app
//            numbers.cancel()
//        }


        //prime number with pipelines
//        fun numbersFrom(context: CoroutineContext, start: Int) = produce<Int>(context) {
//            var x = start
//            while (true) send(x++) // infinite stream of integers from start
//        }
//
//        fun filter(context: CoroutineContext, numbers: ReceiveChannel<Int>, prime: Int) = produce<Int>(context) {
//            for (x in numbers) if (x % prime != 0) send(x)
//        }
//
//        runBlocking<Unit> {
//            var cur = numbersFrom(coroutineContext, 2)
//            for (i in 1..20) {
//                val prime = cur.receive()
//                println(prime)
//                cur = filter(coroutineContext, cur, prime)
//            }
//            coroutineContext.cancelChildren() // cancel all children to let main finish
//        }


//        mBinding.ivAdd.setOnClickListener()
//        {
//            //                        createDialog()
//            toast("hiii")
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        logD("onDestory")
    }


    fun getData(): UserResp = runBlocking {


        var call = ApiClient.service.getUserDetails()


        val deferred = CompletableDeferred<UserResp>()

        deferred.invokeOnCompletion {
            if (deferred.isCancelled) {
                call.cancel()

            }
        }


        call.enqueue(RetrofitCallback {
            lazyProgressView = mBinding.progressBar
            on200Ok { call, response ->
                deferred.complete(response?.body()!!)
            }

        })

        return@runBlocking deferred.await()

    }

}


// this is your first suspending function
suspend fun doWorld() {
    delay(1000)
    println("World!")
}


private fun showMsg(msg: String) {
    println(msg)
}

suspend fun doSomethingUsefulOne(): Int {
    delay(3000L) // pretend we are doing something useful here
    return 21
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}

//    private fun getConsData(): List<ConsResp.Data> {
//        val apiInterface = ApiClient.service
//        val consName = apiInterface.getConstituencyDetail(HashMap(), "123")
//
//        consName.enqueue(this@MainActivity, RetrofitCallback<ConsResp.ConstituencyResp> {
//
//            progressView = mBinding.progressBar
//            onCompleted { call, response, throwable ->
//                progressView?.hide()
//            }
//            on200Ok { call, response ->
//                mConsList = response?.body()?.data
//                Log.d("newCons", response?.body()?.data.toString())
//            }
//        })
//        return mConsList.orEmpty()
//    }

//private fun createDialog() {
//    val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
//    alertDialog.setTitle("Name")
//    alertDialog.setMessage("Enter Name")
//
//    val input = EditText(this@MainActivity)
//    val lp = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT)
//    input.layoutParams = lp
//    alertDialog.setView(input)
//
//    alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener({ dialog: DialogInterface?, which: Int ->
//        mList?.addIfNew(MyListItem(input.value))
//        input.setUppercaseTransformation()
//        mBinding.rvTest.adapter.notifyDataSetChanged()
//        mList?.logD("mList")
//
//    }))
//
//    alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener({ dialog: DialogInterface?, which: Int ->
//        dialog?.cancel()
//    }))
//
//    alertDialog.show()
//}


data class MyListItem(val name: String?)