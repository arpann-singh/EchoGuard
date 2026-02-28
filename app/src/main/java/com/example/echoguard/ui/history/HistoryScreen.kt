package com.example.echoguard.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AlertLog(val id: String, val type: String, val time: String, val status: String)

@Composable
fun HistoryScreen() {
    val logs = listOf(
        AlertLog("EG-2026-001", "Acoustic Detection", "14:20, 24 Feb", "Suppressed"),
        AlertLog("EG-2026-002", "Manual SOS", "23:10, 12 Feb", "Contacts Notified"),
        AlertLog("EG-2026-003", "Fall Detection", "09:45, 05 Feb", "Safe Walk Log")
    )

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Safety History", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Encrypted Evidence Vault", style = MaterialTheme.typography.labelSmall, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(logs) { log ->
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Gavel, null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(log.type, fontWeight = FontWeight.Bold)
                            Text(log.time, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(log.status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}