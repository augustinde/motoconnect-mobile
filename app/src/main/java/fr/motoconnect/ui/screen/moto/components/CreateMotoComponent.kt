package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.viewmodel.MotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMotoComponent(
    motoViewModel: MotoViewModel,
) {

    val motoUIState by motoViewModel.motoUiState.collectAsState()

    var motoName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.add_moto_title),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                modifier = Modifier.padding(0.dp, 20.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = motoName,
            onValueChange = { motoName = it },
            label = { Text(text = stringResource(id = R.string.name_moto_label)) },
            isError = motoUIState.errorMsg != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = {
                motoViewModel.createMoto(motoName)
            }),
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            onClick = {
                motoViewModel.createMoto(motoName)
            }
        ) {
            Text(text = stringResource(id = R.string.add_button))
        }

        if (motoUIState.errorMsg != null) {
            Text(text = motoUIState.errorMsg!!)
        }
        Image(
            painter = painterResource(id = R.drawable.create_moto),
            contentDescription = null
        )
    }
}