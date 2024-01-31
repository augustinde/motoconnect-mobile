package fr.motoconnect.ui.screen.profile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R

@Composable
fun ChangeUsernameLogic(isUsernameError: Boolean) {
    if (isUsernameError) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.username_cannot_be_empty),
            color = MaterialTheme.colorScheme.error,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}