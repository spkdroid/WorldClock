package com.spkd.wordlclock.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.*

/**
 * Custom analog clock view for widgets
 * This draws an analog clock that can be used in RemoteViews
 */
class WidgetAnalogClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var hours: Int = 0
    private var minutes: Int = 0
    private var seconds: Int = 0

    private val clockPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 24f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }

    private val minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
    }

    private val secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 2f
        strokeCap = Paint.Cap.ROUND
    }

    private val centerDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    fun setTime(calendar: Calendar) {
        hours = calendar.get(Calendar.HOUR)
        minutes = calendar.get(Calendar.MINUTE)
        seconds = calendar.get(Calendar.SECOND)
        invalidate()
    }

    fun setTime(hour: Int, minute: Int, second: Int) {
        this.hours = hour % 12
        this.minutes = minute
        this.seconds = second
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(centerX, centerY) - 20f

        // Draw clock face circle
        canvas.drawCircle(centerX, centerY, radius, clockPaint)

        // Draw hour markers and numbers
        for (i in 1..12) {
            val angle = (i * 30 - 90) * PI / 180
            val startX = centerX + cos(angle) * (radius - 30)
            val startY = centerY + sin(angle) * (radius - 30)
            val endX = centerX + cos(angle) * (radius - 15)
            val endY = centerY + sin(angle) * (radius - 15)

            canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), hourHandPaint)

            // Draw numbers
            val numberX = centerX + cos(angle) * (radius - 45)
            val numberY = centerY + sin(angle) * (radius - 45) + 8
            canvas.drawText(i.toString(), numberX.toFloat(), numberY.toFloat(), numberPaint)
        }

        // Draw minute markers
        for (i in 0..59) {
            if (i % 5 != 0) { // Skip hour markers
                val angle = (i * 6 - 90) * PI / 180
                val startX = centerX + cos(angle) * (radius - 15)
                val startY = centerY + sin(angle) * (radius - 15)
                val endX = centerX + cos(angle) * (radius - 10)
                val endY = centerY + sin(angle) * (radius - 10)

                canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), tickPaint)
            }
        }

        // Calculate hand angles
        val hourAngle = ((hours % 12) * 30 + minutes * 0.5 - 90) * PI / 180
        val minuteAngle = (minutes * 6 - 90) * PI / 180
        val secondAngle = (seconds * 6 - 90) * PI / 180

        // Draw hour hand
        val hourEndX = centerX + cos(hourAngle) * (radius * 0.5)
        val hourEndY = centerY + sin(hourAngle) * (radius * 0.5)
        canvas.drawLine(centerX, centerY, hourEndX.toFloat(), hourEndY.toFloat(), hourHandPaint)

        // Draw minute hand
        val minuteEndX = centerX + cos(minuteAngle) * (radius * 0.7)
        val minuteEndY = centerY + sin(minuteAngle) * (radius * 0.7)
        canvas.drawLine(centerX, centerY, minuteEndX.toFloat(), minuteEndY.toFloat(), minuteHandPaint)

        // Draw second hand
        val secondEndX = centerX + cos(secondAngle) * (radius * 0.8)
        val secondEndY = centerY + sin(secondAngle) * (radius * 0.8)
        canvas.drawLine(centerX, centerY, secondEndX.toFloat(), secondEndY.toFloat(), secondHandPaint)

        // Draw center dot
        canvas.drawCircle(centerX, centerY, 12f, centerDotPaint)
    }
}
