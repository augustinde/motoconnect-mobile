package fr.motoconnect.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.motoconnect.R
import fr.motoconnect.data.model.UserObject
import fr.motoconnect.viewmodel.uiState.AuthUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthenticationViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val context: Context
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthUIState())
    val authUiState: StateFlow<AuthUIState> = _authUiState.asStateFlow()

    init {
        if (auth.currentUser != null) {
            _authUiState.value = AuthUIState(isLogged = true)
            viewModelScope.launch {
                getCurrentUser()
            }
        } else {
            _authUiState.value = AuthUIState(isLogged = false)
        }
    }

    private suspend fun getCurrentUser() {
        val user = db.collection("users")
            .document(auth.currentUser!!.uid)
            .get().await()
            .toObject(UserObject::class.java)
        _authUiState.value = AuthUIState(isLogged = true, errorMessage = null, user = user)
    }

    fun signIn(email: String, password: String) {
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.value = AuthUIState(
                isLogged = false,
                errorMessage = context.getString(R.string.mandatory_fields_not_filled),
                user = null
            )
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                _authUiState.value = AuthUIState(
                    isLogged = false,
                    errorMessage = context.getString(R.string.error_email_or_password),
                    user = null
                )
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        if (task.isSuccessful) {
                            viewModelScope.launch {
                                getCurrentUser()
                            }
                        }
                    }
                }
            }
    }

    fun signUp(email: String, password: String, displayName: String, imageProfile: Uri) {
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.value = AuthUIState(
                isLogged = false,
                errorMessage = context.getString(R.string.mandatory_fields_not_filled),
                user = null
            )
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener {
                _authUiState.value = AuthUIState(
                    isLogged = false,
                    errorMessage = context.getString(R.string.error_email_or_password),
                    user = null
                )
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        if (task.isSuccessful) {
                            db.collection("users")
                                .document(auth.currentUser!!.uid)
                                .set(UserObject(displayName = displayName))
                            changeProfilePicture(imageProfile)
                            viewModelScope.launch {
                                getCurrentUser()
                            }
                        }
                    }
                }
            }
    }

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authUiState.value = AuthUIState(
                errorMessage = context.getString(R.string.mandatory_fields_not_filled)
            )
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnFailureListener {
                _authUiState.value = AuthUIState(
                    errorMessage = context.getString(R.string.error_email),
                )
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        if (task.isSuccessful) {
                            _authUiState.value = AuthUIState(
                                errorMessage = context.getString(R.string.reset_password_email_sent)
                            )
                        }
                    }
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authUiState.update { AuthUIState(isLogged = false, errorMessage = null, user = null) }
    }

    fun accountDelete() {
        if (auth.currentUser != null) {
            deleteProfilePicture()
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser!!.delete().addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                _authUiState.update { AuthUIState(isLogged = false, errorMessage = null, user = null) }
                            }
                            else {
                                Log.d("DELETE", context.getString(R.string.accountdelete_failed) + task2.exception!!.message)
                            }
                        }

                    }
                    else {
                        Log.d("DELETE", context.getString(R.string.dbcollection_delete_failed) + task.exception!!.message)
                    }
                }
        } else {
            Log.d("DELETE", context.getString(R.string.no_user_loged_in_user_is_null))
        }


    }

    fun resetErrorMessage() {
        _authUiState.update { AuthUIState(errorMessage = null, user = null) }
    }

    private fun isMandatoryFieldsFilled(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun changeUsername(newUsername: String) {
        db.collection("users").document(auth.currentUser!!.uid)
            .update("displayName", newUsername).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("ChangeUsername", context.getString(R.string.the_username_has_been_changed))
                    Toast.makeText(context, context.getString(R.string.the_username_has_been_changed), Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("ChangeUsername", context.getString(R.string.the_username_has_not_been_changed))
                }
            }
    }

    fun changeProfilePicture(imageUri: Uri) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(auth.currentUser!!.uid + "/profilePicture")

        val uploadTask: UploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnProgressListener {
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Upload", context.getString(R.string.upload_success))
                Toast.makeText(context,
                    context.getString(R.string.the_profile_picture_has_been_changed), Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Upload", context.getString(R.string.upload_failed))
                Toast.makeText(context,
                    context.getString(R.string.the_profile_picture_couldn_t_be_changed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteProfilePicture(){

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(auth.currentUser!!.uid + "/profilePicture")

        storageRef.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Delete", context.getString(R.string.delete_success))
            } else {
                Log.d("Delete", context.getString(R.string.delete_failed))
            }
        }
    }

    fun changePassword(password: String) {
        auth.currentUser!!.updatePassword(password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Password", context.getString(R.string.the_password_has_been_changed))
                Toast.makeText(context, context.getString(R.string.the_password_has_been_changed), Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Password", context.getString(R.string.the_password_has_not_been_changed) + task.exception!!.message)
                Toast.makeText(context, context.getString(R.string.error_the_password_has_not_been_changed_please_login_again), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
