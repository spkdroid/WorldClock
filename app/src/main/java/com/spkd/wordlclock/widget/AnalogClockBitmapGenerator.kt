package com.spkd.wordlclock.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import java.util.*
import kotlin.math.*

/**
 * Utility class to generate analog clock bitmaps for widgets
 */
object AnalogClockBitmapGenerator {

    fun generateClockBitmap(
        context: Context,
        calendar: Calendar,
        size: Int = 200,
        backgroundColor: Int = Color.TRANSPARENT
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        val centerX = size / 2f
        val centerY = size / 2f
        val radius = size / 2f - 20f

        // Paint objects
        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        val clockPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = size * 0.08f
            typeface = Typeface.DEFAULT_BOLD
        }

        val hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = size * 0.025f
            strokeCap = Paint.Cap.ROUND
        }

        val minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = size * 0.02f
            strokeCap = Paint.Cap.ROUND
        }

        val secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = size * 0.008f
            strokeCap = Paint.Cap.ROUND
        }

        val centerDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }

        val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = size * 0.006f
        }

        // Clear background
        if (backgroundColor != Color.TRANSPARENT) {
            canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), backgroundPaint)
        }

        // Draw clock face circle
        canvas.drawCircle(centerX, centerY, radius, clockPaint)

        // Draw hour markers and numbers
        for (i in 1..12) {
            val angle = (i * 30 - 90) * PI / 180
            val startX = centerX + cos(angle) * (radius - radius * 0.15)
            val startY = centerY + sin(angle) * (radius - radius * 0.15)
            val endX = centerX + cos(angle) * (radius - radius * 0.08)
            val endY = centerY + sin(angle) * (radius - radius * 0.08)

            canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), hourHandPaint)

            // Draw numbers
            val numberX = centerX + cos(angle) * (radius - radius * 0.25)
            val numberY = centerY + sin(angle) * (radius - radius * 0.25) + numberPaint.textSize / 3
            canvas.drawText(i.toString(), numberX.toFloat(), numberY.toFloat(), numberPaint)
        }

        // Draw minute markers
        for (i in 0..59) {
            if (i % 5 != 0) { // Skip hour markers
                val angle = (i * 6 - 90) * PI / 180
                val startX = centerX + cos(angle) * (radius - radius * 0.08)
                val startY = centerY + sin(angle) * (radius - radius * 0.08)
                val endX = centerX + cos(angle) * (radius - radius * 0.05)
                val endY = centerY + sin(angle) * (radius - radius * 0.05)

                canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), tickPaint)
            }
        }

        // Get time components
        val hours = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

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
        canvas.drawCircle(centerX, centerY, radius * 0.05f, centerDotPaint)

        return bitmap
    }

    fun generateClockDrawable(context: Context, calendar: Calendar, size: Int = 200): BitmapDrawable {
        val bitmap = generateClockBitmap(context, calendar, size)
        return BitmapDrawable(context.resources, bitmap)
    }
}
