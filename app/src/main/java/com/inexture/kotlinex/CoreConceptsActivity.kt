package com.inexture.kotlinex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.inexture.kotlinex.Example.Companion.staticField

class CoreConceptsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_core_concepts)

        val name = Example.staticFun("vidhi")
        val ex = Example()
        val exname = ex.nonStaticFun("aaa")
        println(exname)
    }

}

class MyClass() {
    constructor(name: String) : this()
}

class Example {
    var nonStatic: String? = ""

    companion object {
        var staticField: String? = ""


        fun staticFun(name: String): String {
            staticField = name
            return staticField as String
        }
    }

    fun nonStaticFun(name: String): String {
        staticField = name
        return staticField as String
    }
}
