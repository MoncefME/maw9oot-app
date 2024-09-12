package com.example.maw9oot.presentation.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.maw9oot.data.enums.PrayerStatus
import com.example.maw9oot.ui.theme.PoppinsFontFamily

@Composable
fun PrayerButton(
    prayerName: String,
    prayerIcon: @Composable () -> Unit,
    prayerStatus: PrayerStatus,
    prayerTime: String?,
    onClick: () -> Unit
) {
    val statusColor = when (prayerStatus) {
        PrayerStatus.MISSED -> Color(0xFFffe5e5)
        PrayerStatus.LATE_ALONE -> Color(0xFFfff1eb)
        PrayerStatus.WITH_GROUP -> Color(0xFFe5f4d9)
        PrayerStatus.ON_TIME_ALONE -> Color(0xFFdff8ff)
        PrayerStatus.NONE -> Color(0xFFf2eeff)
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(100.dp)
            .shadow(5.dp, RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(3f)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    prayerIcon()
                    Spacer(modifier = Modifier.height(8.dp))
                    prayerTime?.let {
                        Text(
                            text = it.substring(0, 5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = prayerName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(statusColor, shape = RoundedCornerShape(0.dp))
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                 painter = painterResource(id =prayerStatus.icon),
                    contentDescription = "Group",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
