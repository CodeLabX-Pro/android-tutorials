package com.example.composebasics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The first screen, built from small composables stacked in a Column:
 * a title, a profile card (a Row), a tap counter (state) and a name box (input).
 */
@Composable
fun HomeScreen(onSayHello: (String) -> Unit) {
    // rememberSaveable keeps these values when the screen redraws, and when the phone rotates.
    var taps by rememberSaveable { mutableIntStateOf(0) }
    var name by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text("Hello, Compose!", fontSize = 34.sp, fontWeight = FontWeight.Bold)

        ProfileCard(name = "Asha Rao", role = "Android learner")

        // State: when taps changes, Compose redraws only the Text that shows it.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("You tapped $taps " + if (taps == 1) "time" else "times", fontSize = 20.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { taps++ }) { Text("Tap me", fontSize = 17.sp) }
                    FilledTonalButton(onClick = { taps = 0 }) { Text("Reset", fontSize = 17.sp) }
                }
            }
        }

        // Input: the text box shows `name`, and every key press updates it.
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = { onSayHello(name.trim()) },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text("Say hello", fontSize = 18.sp)
        }
    }
}

/** A Row puts things side by side: a round initial, then the name and role stacked in a Column. */
@Composable
fun ProfileCard(name: String, role: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(name.take(1), color = MaterialTheme.colorScheme.onPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.size(16.dp))
            Column {
                Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(role, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Previews show a composable in Android Studio's Design view, without running the app. */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ComposeBasicsTheme { HomeScreen(onSayHello = {}) }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    ComposeBasicsTheme { ProfileCard(name = "Asha Rao", role = "Android learner") }
}
