package week11.st221773.matchbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st221773.matchbook.data.repository.AuthRepository
import week11.st221773.matchbook.util.UiState

class MainViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val repo = AuthRepository()


    // UiState for login/auth
    // Tracks whether the user is authenticated, or requires authentication, or is in a loading state.
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        viewModelScope.launch {
            // Add a short delay to allow Firebase to restore session
            delay(1000)// wait 1 second before checking again

            val currentUser = auth.currentUser
            _uiState.value = if (currentUser != null) {
                UiState.Authenticated
            } else {
                UiState.AuthRequired
            }
        }

        // Listen for auth changes
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _uiState.value = if (user != null) {
                UiState.Authenticated
            } else {
                UiState.AuthRequired
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repo.login(email, password)
            if (result.isSuccess) {
                _uiState.value = UiState.Authenticated
            } else {
                _uiState.value = UiState.AuthRequired
                _message.value = result.exceptionOrNull()?.localizedMessage ?: "Login failed"
            }
        }
    }


    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repo.signUp(email, password)

            if (result.isSuccess) {
                // Stay in AuthRequired state
                _uiState.value = UiState.AuthRequired
                _message.value = "Account created! Please log in."
            } else {
                _uiState.value = UiState.AuthRequired
                _message.value = result.exceptionOrNull()?.localizedMessage ?: "Sign up failed"
            }
        }
    }


    fun logout() {
        auth.signOut()
        _uiState.value = UiState.AuthRequired
    }
}