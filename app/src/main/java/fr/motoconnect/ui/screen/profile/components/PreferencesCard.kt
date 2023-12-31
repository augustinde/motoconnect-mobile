package fr.motoconnect.ui.screen.profile.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import fr.motoconnect.R
import fr.motoconnect.data.utils.PermissionUtils
import fr.motoconnect.ui.store.DisplayStore

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PreferencesCard(
    locationPermissionCoarse: PermissionState,
    locationPermission: PermissionState,
    notificationPermission: PermissionState,
    darkmode: Boolean,
    store: DisplayStore,
    context: Context
) {

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
            text = stringResource(R.string.preferences),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Column(
                modifier = Modifier.padding(5.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.location),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                )
                if (locationPermissionCoarse.status.isGranted && !locationPermission.status.isGranted) {
                    Button(
                        onClick = { PermissionUtils().askPermission(locationPermission, context, "location") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        )

                    ) {
                        Text(
                            text = stringResource(R.string.coarse_location_warning),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (!locationPermission.status.isGranted) {
                    Button(
                        onClick = { PermissionUtils().askPermission(locationPermission, context, "location") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.Authorization),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.location_activated),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(
                modifier = Modifier.padding(5.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.notification),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                )
                if (!notificationPermission.status.isGranted) {
                    Button(
                        onClick = { PermissionUtils().askPermission(notificationPermission, context, "notification") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        )

                    ) {
                        Text(
                            text = stringResource(R.string.Authorization),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.notification_activated),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = stringResource(R.string.display),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
            )
            SwitchLogic(darkmode, store)
        }
    }
}