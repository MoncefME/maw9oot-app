package com.example.maw9oot.presentation.ui.components.stats.scoarboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.maw9oot.R

@Composable
fun ScoreInfosDialog(onDismiss: () -> Unit) {
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
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.streak),
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Dark Theme Icon"
                        )
                        Text("Streak", style = MaterialTheme.typography.bodyLarge)

                    }

                    Text("This is the number of days you have completed all your prayers on time. Complete Daily challenge to increase your streak",
                        style = MaterialTheme.typography.bodySmall)
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.score),
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Dark Theme Icon"
                        )
                        Text("Score", style = MaterialTheme.typography.bodyLarge)

                    }
                    Text("This is the total number of points you have based on your prayer statuses",
                        style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Box(
                            Modifier.weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x8884BB58))
                        ){
                            Text("Group (+2)", fontSize = 10.sp, textAlign = TextAlign.Center)
                        }

                        Box(
                            Modifier.weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x8852BFD8))
                        ){
                            Text("Alone  (+1)", fontSize = 10.sp,textAlign = TextAlign.Center)
                        }
                        Box(
                            Modifier.weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x77E49877))
                        ){
                            Text("Late     (-2)", fontSize = 10.sp,textAlign = TextAlign.Center)
                        }
                        Box(
                            Modifier.weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x88DA5B5B))
                        ){
                            Text("Missed (-4)", fontSize = 10.sp,textAlign = TextAlign.Center)
                        }
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.jama3a),
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Dark Theme Icon"
                        )
                        Text("Group", style = MaterialTheme.typography.bodyLarge)

                    }
                    Text("This is the percentage of group prayers you have completed",style = MaterialTheme.typography.bodySmall)

                }

                Button(onClick = { onDismiss() }) {
                    Text(text = "Close")
                }
            }
        }
    }
}
