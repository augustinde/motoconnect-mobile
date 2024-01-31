package fr.motoconnect.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.motoconnect.R
import fr.motoconnect.ui.navigation.MotoConnectNavigationRoutes
import fr.motoconnect.ui.screen.profile.components.ChangePasswordComponent
import fr.motoconnect.ui.screen.profile.components.ChangeProfilePictureComponent
import fr.motoconnect.ui.screen.profile.components.ChangeUsernameComponent
import fr.motoconnect.viewmodel.AuthenticationViewModel

@Composable
fun ModifyProfileScreen(
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController,
    auth: FirebaseAuth
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.modify_profile_picture),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp),
            color= MaterialTheme.colorScheme.tertiary
        )

        ChangeProfilePictureComponent(authenticationViewModel = authenticationViewModel, auth = auth)

        Text(
            text = stringResource(R.string.modify_username),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp),
            color= MaterialTheme.colorScheme.tertiary
        )

        ChangeUsernameComponent(authenticationViewModel = authenticationViewModel)

        Text(
            text = stringResource(R.string.modify_password),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp),
            color= MaterialTheme.colorScheme.tertiary
        )

        ChangePasswordComponent(authenticationViewModel = authenticationViewModel)

        Button(
            onClick = {
                navController.navigate(MotoConnectNavigationRoutes.Profile.name)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.secondary,
            )
        ) {
            Text(text = stringResource(R.string.return_back))
        }

    }

}

