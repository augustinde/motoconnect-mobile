package fr.motoconnect.ui.screen.journey.journeyDetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.PointObject


@Composable
fun PointInfo(
    point: PointObject
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.tertiary)
            .width(200.dp)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color(0xFF343333),
                        radius = 160f,
                    )
                },
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp,
            text = "${point.speed}\nkm/h",
            fontWeight = FontWeight.Bold
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.moto_front),
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .rotate(point.tilt.toFloat()),
            )
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = "${getTilt(point)}°",
                fontSize = 18.sp,
            )
        }
    }
}

private fun getTilt(
    point: PointObject,
): Long {
    return kotlin.math.abs(point.tilt)
}
