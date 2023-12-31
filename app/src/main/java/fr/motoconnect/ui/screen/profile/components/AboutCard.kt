package fr.motoconnect.ui.screen.profile.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.ui.screen.profile.onAppVersion

@Composable
fun AboutCard(context: Context) {

    var showDialogAppInfo by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = Modifier
            .height(210.dp)
            .fillMaxWidth()
    )
    {
        Text(
            text = stringResource(R.string.about),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Button(
                onClick = { showDialogAppInfo = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                )

            ) {
                Text(
                    text = stringResource(R.string.application_infos),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = { onAppVersion(context) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Text(
                    text = stringResource(R.string.application_version),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    if (showDialogAppInfo) {
        AlertDialog(
            onDismissRequest = { showDialogAppInfo = false },
            title = {
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.application_infos),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

            },
            text = {
                Text(
                    text = stringResource(R.string.app_info),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            confirmButton = {
                Button(
                    onClick = { showDialogAppInfo = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 10.dp),
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}