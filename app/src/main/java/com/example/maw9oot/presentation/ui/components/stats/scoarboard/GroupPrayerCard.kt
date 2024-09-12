package com.example.maw9oot.presentation.ui.components.stats.scoarboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maw9oot.R

@Composable
fun GroupPrayerCard(
    currentGroupPercentage  : Int
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.jama3a),
            modifier = Modifier.size(30.dp),
            contentDescription = "Dark Theme Icon"
        )
        Text(
            text= stringResource(id = R.string.STATS_GROUP),
            fontSize = 12.sp
        )
        Text(
            text="$currentGroupPercentage%",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

    }
}