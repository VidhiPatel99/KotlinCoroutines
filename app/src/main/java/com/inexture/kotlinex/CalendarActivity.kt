package com.inexture.kotlinex

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.inexture.kotlinex.databinding.ActivityCalendarBinding
import com.livinglifetechway.k4kotlin.*
import java.util.*
import kotlin.collections.ArrayList


class CalendarActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityCalendarBinding
    var mDate: ArrayList<String> = arrayListOf()
    var mMonths: ArrayList<String> = arrayListOf()
    var mYear: ArrayList<String> = arrayListOf()
    var lastDate: Int? = null
    var mCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBindingView(R.layout.activity_calendar)


        //set date
        mDate.clear()
        lastDate = 31
        mDate.add("dd")
        for (day in 1..lastDate.orZero()) {
            mDate.add(day.toString())
        }

        mBinding.spinnerDate.adapter = HintSpinnerArrayAdapter(this, objects = mDate)

        mBinding.spinnerDate.onItemSelected { parent, view, position, id ->
            try {
                mCalendar.set(Calendar.DATE, mDate[position.orZero() - 1].toInt())
            } catch (e: Exception) {

            }
        }


        //set months
        mMonths.add("mm")
        for (month in 1..12) {
            mMonths.add(String.format("%02d", month))
        }
        mBinding.spinnerMonth.adapter = HintSpinnerArrayAdapter(this, objects = mMonths)
        mBinding.spinnerMonth.onItemSelected { parent, view, position, id ->
            try {
                setDate(position.orZero() - 1, mCalendar.get(Calendar.YEAR))
            } catch (e: Exception) {

            }
        }

        //set year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        mYear.add("yyyy")
        for (year in 1905..currentYear) {
            mYear.add(year.toString())
        }
        mBinding.spinnerYear.adapter = HintSpinnerArrayAdapter(this, objects = mYear)
        mBinding.spinnerYear.onItemSelected { parent, view, position, id ->
            try {
                val selectedYear = mYear[position.orZero()]
                setDate(mCalendar.get(Calendar.MONTH), selectedYear.toInt())
            } catch (e: Exception) {

            }
        }
    }

    private fun setDate(month: Int, year: Int) {
        val selectedDate = mBinding.spinnerDate.selectedItemPosition
        mCalendar.set(Calendar.DATE, 1)

        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.YEAR, year)


        val daysInMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        daysInMonth.logD("daysCount")
        mDate.clear()
        mDate.add("dd")
        for (i in 1..daysInMonth) {
            mDate.add(String.format("%02d", i))
        }
        mBinding.spinnerDate.adapter = HintSpinnerArrayAdapter(this, objects = mDate)


        if (selectedDate > daysInMonth) {
            mBinding.spinnerDate.setSelection(daysInMonth)
        } else {
            mBinding.spinnerDate.setSelection(selectedDate)
        }

    }

}

class HintSpinnerArrayAdapter(
        context: Context, resLayout: Int = android.R.layout.simple_spinner_dropdown_item, objects: List<String>) : ArrayAdapter<String>(context, resLayout, objects) {

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }


    override fun getDropDownView(position: Int, convertView: View,
                                 parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0) {
            tv.setTextColor(Color.GRAY)
        } else {
            tv.setTextColor(Color.BLACK)
        }
        return view
    }
}
