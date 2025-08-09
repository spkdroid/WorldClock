package com.spkd.wordlclock.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.spkd.wordlclock.presentation.components.AnalogClock
import java.time.LocalTime
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spkd.wordlclock.presentation.viewmodel.CurrentTimeInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeZoneCard(
    timeInfo: CurrentTimeInfo,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ...existing code...

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = timeInfo.timeZone.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Apple-style analog clock showing current time
                    val localTime = try {
                        LocalTime.parse(timeInfo.currentTime)
                    } catch (e: Exception) {
                        LocalTime.now()
                    }
                    AnalogClock(
                        time = localTime,
                        clockSize = 64.dp,
                        clockColor = MaterialTheme.colorScheme.primary,
                        handColor = MaterialTheme.colorScheme.onPrimary,
                        secondHandColor = Color.Red,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = timeInfo.currentTime,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timeInfo.dateFormat,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove time zone",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
