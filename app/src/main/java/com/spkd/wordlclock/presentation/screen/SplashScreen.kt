package com.spkd.wordlclock.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spkd.wordlclock.presentation.theme.OrangeAccent
import com.spkd.wordlclock.presentation.theme.RedAccent
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit
) {
    var animationStarted by remember { mutableStateOf(false) }
    
    val clockAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "clock_alpha"
    )
    
    val textAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500),
        label = "text_alpha"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
        delay(2500) // Show splash for 2.5 seconds
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Clock Logo
            Canvas(
                modifier = Modifier
                    .size(120.dp)
                    .alpha(clockAlpha)
            ) {
                drawSplashClock(this)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App Title
            Text(
                text = "World Clock",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp,
                modifier = Modifier.alpha(textAlpha)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Time Around The Globe",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp,
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}

private fun drawSplashClock(drawScope: DrawScope) {
    with(drawScope) {
        val radius = size.minDimension / 2f - 16.dp.toPx()
        val center = Offset(size.width / 2f, size.height / 2f)
        
        // Draw outer circle with gradient effect
        drawCircle(
            color = RedAccent.copy(alpha = 0.1f),
            radius = radius + 8.dp.toPx(),
            center = center
        )
        
        // Draw main clock face
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style = Stroke(width = 4.dp.toPx())
        )
        
        // Draw hour markers
        for (i in 0..11) {
            val angle = (i * 30f - 90f) * (PI / 180f)
            val startRadius = radius * 0.85f
            val endRadius = radius * 0.95f
            
            drawLine(
                color = Color.Black,
                start = Offset(
                    x = center.x + (cos(angle) * startRadius).toFloat(),
                    y = center.y + (sin(angle) * startRadius).toFloat()
                ),
                end = Offset(
                    x = center.x + (cos(angle) * endRadius).toFloat(),
                    y = center.y + (sin(angle) * endRadius).toFloat()
                ),
                strokeWidth = if (i % 3 == 0) 4.dp.toPx() else 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        
        // Draw clock hands pointing to 10:10 (classic watch advertisement time)
        val hourAngle = ((10 + 10 / 60f) * 30f - 90f) * (PI / 180f)
        val minuteAngle = (10 * 6f - 90f) * (PI / 180f)
        
        // Hour hand
        drawLine(
            color = Color.Black,
            start = center,
            end = Offset(
                x = center.x + (cos(hourAngle) * radius * 0.5f).toFloat(),
                y = center.y + (sin(hourAngle) * radius * 0.5f).toFloat()
            ),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        // Minute hand
        drawLine(
            color = Color.Black,
            start = center,
            end = Offset(
                x = center.x + (cos(minuteAngle) * radius * 0.7f).toFloat(),
                y = center.y + (sin(minuteAngle) * radius * 0.7f).toFloat()
            ),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        // Center dot with orange accent
        drawCircle(
            color = OrangeAccent,
            radius = 6.dp.toPx(),
            center = center
        )
        
        // Inner center dot
        drawCircle(
            color = Color.Black,
            radius = 3.dp.toPx(),
            center = center
        )
    }
}
