package fr.motoconnect.ui.screen.profile.components

import android.net.Uri
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import fr.motoconnect.R
import fr.motoconnect.viewmodel.AuthenticationViewModel

@Composable
fun ProfileCard(auth: FirebaseAuth, authenticationViewModel: AuthenticationViewModel) {

    val currentUser = auth.currentUser

    val authUiState by authenticationViewModel.authUiState.collectAsState()

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val storage = FirebaseStorage.getInstance()
    val imageRef = storage.reference.child( auth.currentUser!!.uid + "/profilePicture")

    imageRef.downloadUrl.addOnSuccessListener { uri ->
        imageUri.value = uri
    }

    if (currentUser?.email != null) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.tertiary
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
                    AsyncImage(
                        model = imageUri.value,
                        contentDescription = stringResource(R.string.profile_picture),
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
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                    )
                    Text(
                        text = authUiState.user?.displayName.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.email_textfield_label) + " :",
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                    )
                    Text(
                        text = currentUser.email!!,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
