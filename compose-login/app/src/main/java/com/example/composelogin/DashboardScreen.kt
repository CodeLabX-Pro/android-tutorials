package com.example.composelogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Shown after a successful login. The username arrives through the navigation route. */
@Composable
fun DashboardScreen(username: String, onLogOut: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Hi, $username", fontSize = 34.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 48.dp))
        Text("Here's your week.", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Lessons", "12", Modifier.weight(1f))
            StatCard("Streak", "5 days", Modifier.weight(1f))
        }
        StatCard("Next up", "Navigation in Compose", Modifier.fillMaxWidth())

        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = onLogOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text("Log out", fontSize = 18.sp)
        }
    }
}

/** One coloured tile: a small label above a big value. */
@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    ComposeLoginTheme { DashboardScreen(username = "user", onLogOut = {}) }
}
