package fr.motoconnect.ui.screen.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import firebase.com.protolitewrapper.BuildConfig
import fr.motoconnect.R
import fr.motoconnect.ui.screen.profile.components.AboutCard
import fr.motoconnect.ui.screen.profile.components.ActionCard
import fr.motoconnect.ui.screen.profile.components.PreferencesCard
import fr.motoconnect.ui.screen.profile.components.ProfileCard
import fr.motoconnect.ui.store.DisplayStore
import fr.motoconnect.viewmodel.AuthenticationViewModel

fun onAppVersion(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.version_name) + " : " + BuildConfig.VERSION_NAME + "\n" + context.getString(
            R.string.version_code
        ) + " : " + BuildConfig.VERSION_CODE.toString(),
        Toast.LENGTH_LONG
    ).show()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    authenticationViewModel: AuthenticationViewModel
) {
    val context = LocalContext.current
    val store = DisplayStore(context)
    val darkmode = store.getDarkMode.collectAsState(initial = false)

    val notificationPermission = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    )
    val locationPermission = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val locationPermissionCoarse = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.settings),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                modifier = Modifier.padding(0.dp, 20.dp)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                ProfileCard(auth, authenticationViewModel)
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
                ActionCard(authenticationViewModel)
            }
            item {
                AboutCard(context)
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

}

