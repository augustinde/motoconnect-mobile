package fr.motoconnect.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            .addOnFailureListener() { exception ->
                _authUiState.value = AuthUIState(
                    isLogged = false,
                    errorMessage = exception.message,
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

    fun signUp(email: String, password: String, displayName: String) {
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.value = AuthUIState(
                isLogged = false,
                errorMessage = context.getString(R.string.mandatory_fields_not_filled),
                user = null
            )
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener() { exception ->
                _authUiState.value = AuthUIState(
                    isLogged = false,
                    errorMessage = exception.message,
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
            .addOnFailureListener { exception ->
                _authUiState.value = AuthUIState(
                    errorMessage = exception.message
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

    fun resetErrorMessage() {
        _authUiState.update { AuthUIState(errorMessage = null, user = null) }
    }

    private fun isMandatoryFieldsFilled(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

}
