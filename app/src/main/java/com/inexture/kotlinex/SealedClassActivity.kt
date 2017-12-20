package com.inexture.kotlinex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.R.attr.x
import android.graphics.Path
import android.view.View
import com.inexture.kotlinex.databinding.ActivitySealedClassBinding
import com.livinglifetechway.k4kotlin.setBindingView


//Why use sealed class!!
//http://www.baeldung.com/kotlin-sealed-classes

class SealedClassActivity : AppCompatActivity() {
    lateinit var mBinding: ActivitySealedClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBindingView(R.layout.activity_sealed_class)
        val sum = execute(2, Operation.Add(4))
        val inc = execute(5, Operation.Increment)

        println("Sum : $sum")
        println("Inc : $inc")

        val ui = Ui() + UiOperation.Show + UiOperation.TranslateX(200F) + UiOperation.TranslateY(200F)


        mBinding.btnApply.setOnClickListener {
            applyUiOperation(mBinding.tvHello, ui)
        }

//        val msg = MyMsg("sunday", 2)
//        val newMsg = msg.copy(msgId = 7)
//        println(msg.msgId)
//        println("copiedMsg : $newMsg")

        val msg = MyMsg("sunday")
        msg.msgId = 20
        val copiedMsg = msg.copy(event = "monday")
        println("copiedMsg : $copiedMsg")
        println("copiedMsg : ${copiedMsg.msgId}")


        val event = Test("new event")
        event.eventId = 10
        val copiedEvent = event.copy(event = "copied event")
        println("copiedEvent: ${copiedEvent.eventId}")
        println("copiedEvent: $copiedEvent")

    }

}

sealed class Operation {
    class Add(val value: Int) : Operation()
    class Sub(val value: Int) : Operation()
    class Mul(val value: Int) : Operation()
    class Div(val value: Int) : Operation()
    object Increment : Operation()
    object Decrement : Operation()
}

fun execute(x: Int, ope: Operation) = when (ope) {
    is Operation.Add -> x + ope.value
    is Operation.Sub -> x - ope.value
    is Operation.Mul -> x * ope.value
    is Operation.Div -> x / ope.value
    Operation.Increment -> x + 1
    Operation.Decrement -> x - 1
}


sealed class UiOperation {
    object Show : UiOperation()
    object Hide : UiOperation()
    class TranslateX(val px: Float) : UiOperation()
    class TranslateY(val py: Float) : UiOperation()
}

fun executeUi(view: View, uiOperation: UiOperation) = when (uiOperation) {
    UiOperation.Show -> view.visibility = View.VISIBLE
    UiOperation.Hide -> view.visibility = View.INVISIBLE
    is UiOperation.TranslateX -> view.translationX = uiOperation.px
    is UiOperation.TranslateY -> view.translationY = uiOperation.py
}

class Ui(val uiOps: List<UiOperation> = arrayListOf()) {
    operator fun plus(uiOperation: UiOperation) = Ui(uiOps + uiOperation)
}

fun applyUiOperation(view: View, ui: Ui) {
    ui.uiOps.forEach { executeUi(view, it) }
}

sealed class Msg() {
    var msgId: Int = 5
}

open class SimpleClass() {
    open var eventId: Int = 15
}

data class MyMsg(val event: String) : Msg()

data class Test(val event: String) : SimpleClass()