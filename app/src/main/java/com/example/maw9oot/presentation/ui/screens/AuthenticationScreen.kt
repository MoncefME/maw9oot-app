package com.example.maw9oot.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.maw9oot.R


@Composable
fun AuthenticationScreen(onAuthenticateClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.baseline_fingerprint_24),
                contentDescription = "Biometric Authentication"
            )
            Text(
                text = "You need to Authenticate to access Maw9oot",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Button(onClick = onAuthenticateClick) {
                Text(text = "Authenticate")
            }
        }
    }
}
