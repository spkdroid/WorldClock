package com.spkd.wordlclock.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClock(
    time: LocalTime,
    modifier: Modifier = Modifier,
    clockSize: Dp = 40.dp,
    clockColor: Color = Color.Black,
    handColor: Color = Color.Black,
    secondHandColor: Color = Color.Red
) {
    Canvas(modifier = modifier.size(clockSize)) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Draw clock face
        drawCircle(
            color = clockColor.copy(alpha = 0.08f),
            radius = radius,
            center = center
        )
        drawCircle(
            color = clockColor,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw hour and minute hands
        val hour = time.hour % 12
        val minute = time.minute
        val second = time.second

        val hourAngle = ((hour + minute / 60f) * 30f - 90f) * (PI / 180f)
        val minuteAngle = ((minute + second / 60f) * 6f - 90f) * (PI / 180f)
        val secondAngle = (second * 6f - 90f) * (PI / 180f)

        // Hour hand
        drawLine(
            color = handColor,
            start = center,
            end = Offset(
                x = center.x + (cos(hourAngle) * radius * 0.5f).toFloat(),
                y = center.y + (sin(hourAngle) * radius * 0.5f).toFloat()
            ),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Minute hand
        drawLine(
            color = handColor,
            start = center,
            end = Offset(
                x = center.x + (cos(minuteAngle) * radius * 0.75f).toFloat(),
                y = center.y + (sin(minuteAngle) * radius * 0.75f).toFloat()
            ),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Second hand
        drawLine(
            color = secondHandColor,
            start = center,
            end = Offset(
                x = center.x + (cos(secondAngle) * radius * 0.85f).toFloat(),
                y = center.y + (sin(secondAngle) * radius * 0.85f).toFloat()
            ),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Center dot
        drawCircle(
            color = handColor,
            radius = 3.dp.toPx(),
            center = center
        )
    }
}
