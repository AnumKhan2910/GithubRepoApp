package com.example.githubrepoapp.utils

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.githubrepoapp.R
import java.util.*

class BarChart : FrameLayout {
    private var barTextColor = 0
    private var barDimension = 0
    private var barTextSize = 0
    private var barColor = 0
    private var barMaxValue = 0
    private var LinearParent: LinearLayout? = null

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0)
        barDimension = a.getDimensionPixelSize(
            R.styleable.BarChart_bar_width,
            convertDpToPixel(20f, context).toInt()
        )
        barColor = a.getColor(R.styleable.BarChart_bar_color, Color.BLACK)
        barTextSize = a.getDimensionPixelSize(
            R.styleable.BarChart_bar_text_size,
            convertDpToPixel(13f, context).toInt()
        )
        barTextSize = convertPixelsToDp(barTextSize.toFloat(), context).toInt()
        barTextColor = a.getColor(
            R.styleable.BarChart_bar_text_color,
            Color.BLACK
        )

        barMaxValue = a.getInt(R.styleable.BarChart_bar_max_value, 0)

        init()
    }


    fun setBarMaxValue(barMaxValue: Int) {
        this.barMaxValue = barMaxValue
        clearAll()
    }

    private fun init() {
        initBarChart()
    }

    private fun initBarChart() {
        LinearParent = LinearLayout(context)
        LinearParent!!.orientation = LinearLayout.HORIZONTAL
        LinearParent!!.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        LinearParent!!.layoutTransition = LayoutTransition()
        this.addView(LinearParent)
    }


    private fun createBarChart(
        dimension: Int,
        text: String,
        value: Int,
        unit: String
    ) {
        if (dimension == 0 || barMaxValue == 0) {
            return
        }
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_bar_chart, LinearParent, false)
        updateUi(dimension, text, value, unit, view)
    }


    private fun updateUi(
        dimension: Int, text: String, value: Int, unit: String,
        view: View
    ) {
        view.findViewById<View>(R.id.linear_bar).setBackgroundColor(barColor)
        val dimensionBar: Int = dimension * value / barMaxValue
        val textView1 = view.findViewById<TextView>(R.id.barText)
        val textView2 = view.findViewById<TextView>(R.id.barValue)

        textView1.text = String.format(
            Locale.getDefault(),
            "%s",
            text
        )
        textView2.text = String.format(
            Locale.getDefault(),
            "%d",
            value
        ) + "  " + unit

        textView1.textSize = barTextSize.toFloat()
        textView1.setTextColor(barTextColor)

        val linearLayoutBar = view.findViewById<LinearLayout>(R.id.linear_bar)
        val anim = ValueAnimator.ofInt( 0,
            dimensionBar
        )
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = linearLayoutBar.layoutParams
            layoutParams.height = `val`
            linearLayoutBar.layoutParams = layoutParams
        }
        anim.duration = 500
        anim.start()
        view.findViewById<LinearLayout>(R.id.parent).layoutParams.width = barDimension
        LinearParent!!.addView(view)
    }


    private fun getDimension(
        isHeightRequested: Boolean,
        view: View?,
        listener: DimensionReceivedCallback
    ) {
        view!!.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                if (isHeightRequested) {
                    listener.onDimensionReceived(view.height)
                } else {
                    listener.onDimensionReceived(view.width)
                }
            }
        })
    }


    fun addBar(barText: String, barValue: Int, unit: String) {
        if (LinearParent!!.height == 0) {
            getDimension(
                true,
                LinearParent,
                object : DimensionReceivedCallback {
                    override fun onDimensionReceived(dimension: Int) {
                        createBarChart(dimension, barText, barValue, unit)
                    }
                })
        } else {
            createBarChart(LinearParent!!.height, barText, barValue, unit)
        }
    }


    fun clearAll() {
        if (LinearParent != null) {
            LinearParent!!.removeAllViews()
        }

    }


    private fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    private fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    private interface DimensionReceivedCallback {
        fun onDimensionReceived(dimension: Int)
    }

}