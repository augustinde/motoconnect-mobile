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
import fr.motoconnect.data.model.ButtonPasswordUsernameState

@Composable
fun ChangePasswordDisplay(buttonStatePassword: ButtonPasswordUsernameState) {
    Text(
        text = when (buttonStatePassword) {
            ButtonPasswordUsernameState.EDIT -> stringResource(R.string.edit)
            ButtonPasswordUsernameState.UPDATE -> stringResource(R.string.update_password)
        }
    )
}

@Composable
fun ChangePasswordErrors(isNotEqual: Boolean, isEmpty: Boolean){
    if (isNotEqual) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.passwords_are_not_the_same),
            color = MaterialTheme.colorScheme.error,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    } else if (isEmpty) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.password_cannot_be_empty),
            color = MaterialTheme.colorScheme.error,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}