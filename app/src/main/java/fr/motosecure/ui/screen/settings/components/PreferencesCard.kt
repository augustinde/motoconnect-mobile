package fr.motosecure.ui.screen.settings.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import fr.motosecure.R
import fr.motosecure.data.utils.PermissionUtils
import fr.motosecure.ui.store.DisplayStore

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PreferencesCard(
    locationPermissionCoarse: PermissionState,
    locationPermission: PermissionState,
    darkmode: Boolean,
    store: DisplayStore,
    context: Context
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = Modifier
            .fillMaxWidth()
    )
    {
        Text(
            text = stringResource(R.string.preferences),
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
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
            )
            SwitchLogic(darkmode, store)
        }
    }
}