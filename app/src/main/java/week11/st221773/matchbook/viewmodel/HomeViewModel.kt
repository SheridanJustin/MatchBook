import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import week11.st221773.matchbook.model.Book
import week11.st221773.matchbook.network.RetrofitClient
import week11.st221773.matchbook.data.repository.BookRepository
import week11.st221773.matchbook.data.repository.GoogleBooksRepository

open class HomeViewModel : ViewModel() {

    var recommendations = mutableStateListOf<Book>()

    var isLoading = mutableStateOf(false)
        private set

    private val googleRepo = GoogleBooksRepository()

    fun fetchBooks(prompt: String) {
        viewModelScope.launch {
            isLoading.value = true

            // clear old results if any
            recommendations.clear()

            try {
                val response = RetrofitClient.api.getRecommendations(
                    mapOf("prompt" to prompt)
                )

                // enrich response
                val enriched = response.books.map { rawBook ->
                    googleRepo.enrichBook(rawBook)
                }

                // Print json from server
                Log.d("API_JSON", response.toString())

                recommendations.clear()

                recommendations.addAll(enriched)
            } catch (e: Exception) {
                recommendations.clear()
                recommendations.add(
                    Book(
                        title = "Error",
                        author = "",
                        reason = e.message ?: "Unknown error"
                    )
                )
            }
            isLoading.value = false
        }
    }
}


