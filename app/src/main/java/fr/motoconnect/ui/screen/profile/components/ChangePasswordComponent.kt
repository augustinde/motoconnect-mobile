package fr.motoconnect.ui.screen.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fr.motoconnect.R
import fr.motoconnect.data.model.ButtonPasswordUsernameState
import fr.motoconnect.viewmodel.AuthenticationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordComponent(
    authenticationViewModel: AuthenticationViewModel
) {
    val password = remember { mutableStateOf("") }

    val confirmationPassword = remember { mutableStateOf("") }

    val isEditable = remember { mutableStateOf(false) }

    val isPasswordError = remember { mutableStateOf(false) }

    val isEmpty = remember { mutableStateOf(false) }

    val isNotEqual = remember { mutableStateOf(false) }

    val buttonStatePassword = remember { mutableStateOf(ButtonPasswordUsernameState.EDIT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = {
                Text(
                    text = stringResource(R.string.new_password),
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            readOnly = !isEditable.value,
            isError = isPasswordError.value,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = confirmationPassword.value,
            onValueChange = { confirmationPassword.value = it },
            label = {
                Text(
                    text = stringResource(R.string.confirm_password),
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            readOnly = !isEditable.value,
            isError = isPasswordError.value,
            visualTransformation = PasswordVisualTransformation()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 15.dp)
        ) {
            Button(
                onClick = {
                    when (buttonStatePassword.value) {
                        ButtonPasswordUsernameState.EDIT -> {
                            isEditable.value = !isEditable.value
                            buttonStatePassword.value = ButtonPasswordUsernameState.UPDATE
                        }

                        ButtonPasswordUsernameState.UPDATE -> {
                            if (confirmationPassword.value != password.value) {
                                isNotEqual.value = true
                                isPasswordError.value = true
                            } else if (password.value.isEmpty()) {
                                isEmpty.value = true
                                isPasswordError.value = true
                            } else {
                                isNotEqual.value = false
                                isEmpty.value = false
                                isPasswordError.value = false
                                authenticationViewModel.changePassword(password.value)
                                isEditable.value = false
                                buttonStatePassword.value = ButtonPasswordUsernameState.EDIT
                            }
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                ChangePasswordDisplay(buttonStatePassword = buttonStatePassword.value)
            }
        }
        ChangePasswordErrors(isNotEqual = isNotEqual.value, isEmpty = isEmpty.value)
    }
}