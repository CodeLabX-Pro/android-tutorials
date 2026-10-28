package com.example.composebasics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** The second screen. It gets the name from the route, and calls onBack to return. */
@Composable
fun GreetingScreen(name: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("👋", fontSize = 72.sp)
        Text("Hello, $name!", fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("You just built your first Compose app.", fontSize = 18.sp, textAlign = TextAlign.Center)
        Button(onClick = onBack) { Text("Back", fontSize = 17.sp) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingScreenPreview() {
    ComposeBasicsTheme { GreetingScreen(name = "Asha", onBack = {}) }
}
