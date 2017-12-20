package com.inexture.kotlinex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.SeekBar
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.animation.Easing
import android.graphics.Color
import android.view.WindowManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.inexture.kotlinex.databinding.ActivityChartBinding
import com.livinglifetechway.k4kotlin.setBindingView
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.livinglifetechway.k4kotlin.orZero


class ChartActivity : AppCompatActivity(), OnChartValueSelectedListener {
    //    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
////        tvX?.setText("" + (mSeekBarX?.getProgress()));
////        tvY?.setText("" + (mSeekBarY?.getProgress()));
//
////        setData(mSeekBarX?.progress.orZero(), mSeekBarY?.progress?.toFloat().orZero());
//    }
//
//    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//    }
//
//    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//    }
//
    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    private var mChart: PieChart? = null
    //    private var mSeekBarX: SeekBar? = null
//    var mSeekBarY: SeekBar? = null
    private var tvX: TextView? = null
    var tvY: TextView? = null
    lateinit var mBinding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBindingView(R.layout.activity_chart)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

//        tvX = mBinding.tvXMax
//        tvY = mBinding.tvYMax

//        mSeekBarX = mBinding.seekBar1
//        mSeekBarY = mBinding.seekBar2
//        mSeekBarX?.progress = 4
//        mSeekBarY?.progress = 10

        mChart = mBinding.chart1
        mChart?.setUsePercentValues(true)
        mChart?.description?.isEnabled = false
        mChart?.setExtraOffsets(5F, 10F, 5F, 5F)

        mChart?.dragDecelerationFrictionCoef = 0.95f


//        mChart?.setCenterTextTypeface(mTfLight)
//        mChart?.centerText = generateCenterSpannableText()

//        mChart?.isDrawHoleEnabled = true
//        mChart?.setHoleColor(Color.WHITE)

//        mChart?.setTransparentCircleColor(Color.WHITE)
        mChart?.setTransparentCircleAlpha(110)

        mChart?.holeRadius = 30f
        mChart?.transparentCircleRadius = 0f


        mChart?.setDrawCenterText(false)

        mChart?.rotationAngle = 0F
        // enable rotation of the chart by touch
        mChart?.isRotationEnabled = true
        mChart?.isHighlightPerTapEnabled = true
        mChart?.setDrawEntryLabels(false)

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart?.setOnChartValueSelectedListener(this)

        setData(100F)

        mChart?.animateY(1400, Easing.EasingOption.EaseInOutQuad)
        // mChart.spin(2000, 0, 360);

//        mSeekBarX?.setOnSeekBarChangeListener(this)
//        mSeekBarY?.setOnSeekBarChangeListener(this)

        val l = mChart?.legend
        l?.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l?.orientation = Legend.LegendOrientation.HORIZONTAL
        l?.isWordWrapEnabled = true
        l?.setDrawInside(false)
        l?.xEntrySpace = 7f
        l?.yEntrySpace = 0f
        l?.yOffset = 7f

        // entry label styling
        mChart?.setEntryLabelColor(Color.BLACK)
        mChart?.setEntryLabelTextSize(12f)
    }

    private fun setData(range: Float) {

        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        val mParties = arrayListOf<String>("party A", "party B", "party C", "party D", "party E", "party F", "party G", "party H", "party I")
        for (i in 0 until mParties.size) {
            entries.add(PieEntry((Math.random() * range + range / 5).toFloat(), mParties[i % mParties.size]))
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)


        dataSet.sliceSpace = 1f

        dataSet.selectionShift = 5f


        // add a lot of colors

        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

//        data.setValueTypeface(mTfLight)
        mChart?.data = data

        // undo all highlights
        mChart?.highlightValues(null)

        mChart?.invalidate()
    }
}
