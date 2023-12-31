package fr.motoconnect.ui.screen.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import fr.motoconnect.R
import fr.motoconnect.viewmodel.AuthenticationViewModel

@Composable
fun ProfileCard(auth: FirebaseAuth, authenticationViewModel: AuthenticationViewModel) {

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
