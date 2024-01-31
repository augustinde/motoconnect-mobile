package fr.motoconnect.ui.screen.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import fr.motoconnect.viewmodel.AuthenticationViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.stringResource
import fr.motoconnect.R
import fr.motoconnect.data.model.ButtonProfilePictureState

@Composable
fun ChangeProfilePictureComponent(
    authenticationViewModel: AuthenticationViewModel,
    auth: FirebaseAuth
) {

    val buttonStateProfilePicture = remember { mutableStateOf(ButtonProfilePictureState.PICK) }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val storage = FirebaseStorage.getInstance()
    val imageRef = storage.reference.child(auth.currentUser!!.uid + "/profilePicture")

    LaunchedEffect(Unit) {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUri = uri
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = null
                imageUri = it
            }
        }
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 15.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp, 100.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    when (buttonStateProfilePicture.value) {
                        ButtonProfilePictureState.PICK -> {
                            galleryLauncher.launch("image/*")
                            buttonStateProfilePicture.value = ButtonProfilePictureState.UPLOAD
                        }
                        ButtonProfilePictureState.UPLOAD -> {
                            if (imageUri != null) {
                                authenticationViewModel.changeProfilePicture(imageUri!!)
                                buttonStateProfilePicture.value = ButtonProfilePictureState.PICK
                            }
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Text(
                    text = when (buttonStateProfilePicture.value) {
                        ButtonProfilePictureState.PICK -> stringResource(R.string.pick_image)
                        ButtonProfilePictureState.UPLOAD -> stringResource(R.string.update_profile_picture)
                    }
                )
            }
        }
    }
}
