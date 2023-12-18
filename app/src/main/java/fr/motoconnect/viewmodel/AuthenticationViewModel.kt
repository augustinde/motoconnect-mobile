package fr.motoconnect.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import fr.motoconnect.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val context: Context
) : ViewModel() {

    val TAG = "AuthenticationViewModel"

    private val _authUiState = MutableStateFlow(AuthUIState())
    val authUiState: StateFlow<AuthUIState> = _authUiState.asStateFlow()

    init {
        if (auth.currentUser != null) {
            _authUiState.value = AuthUIState(isLogged = true)
        } else {
            _authUiState.value = AuthUIState(isLogged = false)
        }
    }

    fun signIn(email: String, password: String) {
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.value = AuthUIState(
                isLogged = false,
                errorMessage = context.getString(R.string.mandatory_fields_not_filled)
            )
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener() { exception ->
                _authUiState.value = AuthUIState(
                    isLogged = false,
                    errorMessage = exception.message
                )
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        if (task.isSuccessful) {
                            _authUiState.value = AuthUIState(isLogged = true, errorMessage = null)
                        }
                    }
                }
            }
    }

    fun signUp(email: String, password: String) {
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.value = AuthUIState(
                isLogged = false,
                errorMessage = context.getString(R.string.mandatory_fields_not_filled)
            )
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener() { exception ->
                _authUiState.value = AuthUIState(
                    isLogged = false,
                    errorMessage = exception.message
                )
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        if (task.isSuccessful) {
                            _authUiState.value = AuthUIState(isLogged = true, errorMessage = null)
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
        _authUiState.update { AuthUIState(isLogged = false, errorMessage = null) }
    }

    fun resetErrorMessage() {
        _authUiState.update { AuthUIState(errorMessage = null) }
    }

    private fun isMandatoryFieldsFilled(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

}

data class AuthUIState(
    val isLogged: Boolean = false,
    val errorMessage: String? = null
)