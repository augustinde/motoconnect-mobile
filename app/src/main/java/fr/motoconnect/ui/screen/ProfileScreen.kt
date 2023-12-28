package fr.motoconnect.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import firebase.com.protolitewrapper.BuildConfig
import fr.motoconnect.R
import fr.motoconnect.ui.store.DisplayStore
import fr.motoconnect.ui.theme.MotoConnectTheme
import fr.motoconnect.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun onAccountDelete() {
    //A faire plus tard
}

fun onAppVersion(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.version_name) + " : " + BuildConfig.VERSION_NAME + "\n"
                + context.getString(R.string.version_code) + " : " + BuildConfig.VERSION_CODE.toString(),
        Toast.LENGTH_LONG
    ).show()
}

@OptIn(ExperimentalPermissionsApi::class)
fun askNotification(notificationPermission: PermissionState,context: Context) {
    if (!notificationPermission.status.isGranted) {
        Log.d("NOTIF", context.getString(R.string.AskNotificiation))
        notificationPermission.launchPermissionRequest()

    } else {
        Log.d("NOTIF", context.getString(R.string.AcceptedNotification))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun askLocation(locationPermission: PermissionState,context: Context) {
    if (!locationPermission.status.isGranted) {
        Log.d("NOTIF", context.getString(R.string.askLocation))
        locationPermission.launchPermissionRequest()

    } else {
        Log.d("NOTIF", context.getString(R.string.acceptedLocation))
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    authenticationViewModel: AuthenticationViewModel
) {
    val context = LocalContext.current
    val store = DisplayStore(context)
    val darkmode = store.getDarkMode.collectAsState(initial =false)

    val notificationPermission = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    )
    val locationPermission = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val locationPermissionCoarse = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    MotoConnectTheme(activated = darkmode.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(R.string.settings),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                ProfileCard(auth,authenticationViewModel)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                PreferencesCard(
                    locationPermissionCoarse,
                    locationPermission,
                    notificationPermission,
                    darkmode.value,
                    store,
                    context
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                ActionCard(authenticationViewModel)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                AboutCard(context)
            }
        }
    }

}

@Composable
fun ProfileCard(auth: FirebaseAuth,authenticationViewModel: AuthenticationViewModel) {

    val currentUser = auth.currentUser

    val authUiState by authenticationViewModel.authUiState.collectAsState()

    if (currentUser?.email != null) {
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
                text = stringResource(R.string.profile),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(14.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.moto),
                        contentDescription = stringResource(R.string.profile_picture),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .clip(shape = CircleShape)
                    )
                }
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = stringResource(R.string.username) + " :",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                    )
                    Text(
                        text = authUiState.user?.displayName.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.email_textfield_label) + " :",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                    )
                    Text(
                        text = currentUser.email!!,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

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
                        onClick = { askLocation(locationPermission, context) },
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
                        onClick = { askLocation(locationPermission, context) },
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
                        onClick = { askNotification(notificationPermission, context) },
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

@Composable
fun SwitchLogic(darkmode: Boolean, store: DisplayStore){
    Switch(
        checked = darkmode,
        onCheckedChange = {
            CoroutineScope(Dispatchers.IO).launch{
                store.setDarkMode(it)
            }
        },
        thumbContent = if (darkmode) {
            {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.secondary,
            checkedTrackColor = MaterialTheme.colorScheme.secondary,
            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
        )
    )
}

@Composable
fun ActionCard(authenticationViewModel: AuthenticationViewModel) {
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
            text = stringResource(R.string.actions),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.sign_out),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        authenticationViewModel.signOut()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    )

                ) {
                    Text(
                        text = stringResource(id = R.string.sign_out),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = stringResource(R.string.account_deletion),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = { onAccountDelete() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    )
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

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