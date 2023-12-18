package fr.motoconnect.ui.screen.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.motoconnect.R
import fr.motoconnect.ui.navigation.AuthenticationNavigationRoutes
import fr.motoconnect.viewmodel.AuthenticationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController
) {

    val authUiState by authenticationViewModel.authUiState.collectAsState()

    val email = remember { mutableStateOf(String()) }

    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.tertiary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = stringResource(id = R.string.reset_password_title),
            style = TextStyle(fontSize = 30.sp, color = MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(20.dp))
        TextField(
            value = email.value,
            onValueChange = email::value::set,
            isError = authUiState.errorMessage != null,
            label = { Text(text = stringResource(id = R.string.email_textfield_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { authenticationViewModel.resetPassword(email.value) }
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    authenticationViewModel.resetPassword(email.value)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(id = R.string.reset_password_button))
            }
        }
        if (authUiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = authUiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(
            onClick = {
                navController.navigate(AuthenticationNavigationRoutes.SignIn.name)
                authenticationViewModel.resetErrorMessage()
            },
        ) {
            Text(text = stringResource(id = R.string.back_to_sign_in))
        }
    }
}