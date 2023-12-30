package fr.motoconnect.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Loading(){
    CircularProgressIndicator(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp),
        strokeWidth = 8.dp,
        color = MaterialTheme.colorScheme.tertiary,
    )
}