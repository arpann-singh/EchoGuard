package com.example.echoguard.ui.dashboard

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.echoguard.data.service.EchoGuardService
import com.example.echoguard.emergency.SosManager
import com.example.echoguard.utils.PermissionHelper

/**
 * DashboardScreen: The central tactical shield.
 * FIX: Package corrected to ui.dashboard (no 'screens' folder).
 */
@Composable
fun DashboardScreen(navController: NavController) {
    var monitoringEnabled by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val sosManager = remember { SosManager(context) }

    PermissionHelper.RequestPermissions {
        // Permissions secured
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ECHOGUARD SHIELD",
            style = MaterialTheme.typography.headlineMedium,
            color = if (monitoringEnabled) Color(0xFF00C853) else Color.Red
        )

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            onClick = {
                if (PermissionHelper.hasAllPermissions(context)) {
                    sosManager.executeEmergencySequence("Manual Trigger")
                }
            },
            modifier = Modifier
                .size(240.dp)
                .border(width = 4.dp, color = Color.Red, shape = CircleShape),
            shape = CircleShape,
            color = Color.Red.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Warning, null, modifier = Modifier.size(80.dp), tint = Color.Red)
                Text(
                    text = "TAP TO SOS",
                    modifier = Modifier.offset(y = 60.dp),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (monitoringEnabled) Color(0xFF001A00) else Color(0xFF1A0000)
            )
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (monitoringEnabled) "AI GUARD ACTIVE" else "AI GUARD DISABLED",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text("Monitoring for threats...", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                }
                Switch(
                    checked = monitoringEnabled,
                    onCheckedChange = { isEnabled ->
                        if (PermissionHelper.hasAllPermissions(context)) {
                            monitoringEnabled = isEnabled
                            val intent = Intent(context, EchoGuardService::class.java)
                            if (isEnabled) context.startForegroundService(intent) else context.stopService(intent)
                        }
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00C853))
                )
            }
        }
    }
}