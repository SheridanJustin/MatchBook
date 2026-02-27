package week11.st221773.matchbook.util

sealed class UiState {

    object Loading : UiState()
    object AuthRequired : UiState()
    object Authenticated : UiState()

    // Add more later
    data class Error(val message: String) : UiState()
}