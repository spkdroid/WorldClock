package com.spkd.wordlclock.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spkd.wordlclock.presentation.viewmodel.CurrentTimeInfo
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeZoneCard(
    timeInfo: CurrentTimeInfo,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // City name and time info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = timeInfo.timeZone.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Apple-style analog clock showing current time
                    val localTime = try {
                        LocalTime.parse(timeInfo.currentTime)
                    } catch (e: Exception) {
                        LocalTime.now()
                    }
                    AnalogClock(
                        time = localTime,
                        clockSize = 80.dp,
                        clockColor = MaterialTheme.colorScheme.primary,
                        handColor = MaterialTheme.colorScheme.primary,
                        secondHandColor = Color(0xFFE53E3E), // Red accent
                    )
                    
                    Column {
                        AirportBoardTime(time = timeInfo.currentTime)
                        Text(
                            text = timeInfo.dateFormat,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Remove button with professional styling
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE53E3E).copy(alpha = 0.1f)
                ),
                modifier = Modifier.size(48.dp)
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove time zone",
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AirportBoardTime(time: String) {
    // Parse the time string (assuming format like "14:30:45" or "2:30:45 PM")
    val cleanTime = time.replace(Regex("[^0-9:]"), "").take(8) // Remove AM/PM and keep only digits and colons
    val timeParts = cleanTime.split(":")
    
    if (timeParts.size >= 2) {
        val hour = timeParts[0].padStart(2, '0')
        val minute = timeParts[1].padStart(2, '0')
        val second = if (timeParts.size > 2) timeParts[2].padStart(2, '0') else "00"

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(6.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF2A2D33), Color(0xFF1A1D22))
                    ),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            AnimatedDigitPair(digit = hour)
            FlipSeparator()
            AnimatedDigitPair(digit = minute)
            FlipSeparator()
            AnimatedDigitPair(digit = second)
        }
    } else {
        // Fallback to simple text if parsing fails
        Text(
            text = time,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AnimatedDigitPair(digit: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedDigit(digit = digit.getOrNull(0)?.toString() ?: "0")
        AnimatedDigit(digit = digit.getOrNull(1)?.toString() ?: "0")
    }
}

@Composable
fun AnimatedDigit(digit: String) {
    var previousDigit by remember { mutableStateOf(digit) }
    var currentDigit by remember { mutableStateOf(digit) }
    var isAnimating by remember { mutableStateOf(false) }
    
    // Trigger animation when digit changes
    LaunchedEffect(digit) {
        if (digit != currentDigit && !isAnimating) {
            previousDigit = currentDigit
            isAnimating = true
            currentDigit = digit
        }
    }
    
    val animationProgress by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            if (it == 1f) {
                isAnimating = false
            }
        },
        label = "digitFlip"
    )
    
    Box(
        modifier = Modifier.size(width = 12.dp, height = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Previous digit (top half fading out)
        if (isAnimating && animationProgress < 0.5f) {
            Text(
                text = previousDigit,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFAFFFD).copy(alpha = 1f - (animationProgress * 2f))
                ),
                modifier = Modifier.graphicsLayer {
                    rotationX = animationProgress * 90f
                    cameraDistance = 12 * density
                }
            )
        }
        
        // Current digit (bottom half fading in)
        if (!isAnimating || animationProgress >= 0.5f) {
            Text(
                text = currentDigit,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (isAnimating) {
                        Color(0xFFFAFFFD).copy(alpha = (animationProgress - 0.5f) * 2f)
                    } else {
                        Color(0xFFFAFFFD)
                    }
                ),
                modifier = Modifier.graphicsLayer {
                    rotationX = if (isAnimating) {
                        -90f + ((animationProgress - 0.5f) * 180f)
                    } else {
                        0f
                    }
                    cameraDistance = 12 * density
                }
            )
        }
    }
}

@Composable
fun FlipSeparator() {
    Text(
        text = ":",
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFFAFFFD)
        ),
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}
