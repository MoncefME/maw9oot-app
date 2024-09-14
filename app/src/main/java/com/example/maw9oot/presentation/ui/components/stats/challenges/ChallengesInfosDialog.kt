package com.example.maw9oot.presentation.ui.components.stats.challenges

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.maw9oot.R

@Composable
fun ChallengesInfosDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,

                        ){
                            Image(
                                painter = painterResource(id = R.drawable.daily),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Dark Theme Icon"
                            )
                            Text("Daily Challenge",style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)

                        }

                        Text("Complete All prayers of the day with Group or ontime alone , Streak is based on this Challenge",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,

                            ){
                            Image(
                                painter = painterResource(id = R.drawable.weekly),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Dark Theme Icon"
                            )
                            Text("Weekly Fajr Challenge",style = MaterialTheme.typography.bodyLarge,textAlign = TextAlign.Center)

                        }

                        Text("Complete Fajr Challenge with group or ontime for a week",style = MaterialTheme.typography.bodyMedium)

                    }
                }

                Button(onClick = { onDismiss() }) {
                    Text(text = "Close")
                }
            }
        }
    }
}
