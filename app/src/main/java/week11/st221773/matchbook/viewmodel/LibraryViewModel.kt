package week11.st221773.matchbook.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import week11.st221773.matchbook.data.repository.BookRepository
import week11.st221773.matchbook.model.Book

class LibraryViewModel(
    private val bookRepo: BookRepository = BookRepository()
) : ViewModel() {

    var favorites = mutableStateListOf<Book>()
        private set

    var toBeRead = mutableStateListOf<Book>()
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun loadLibrary() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            favorites.clear()
            toBeRead.clear()

            val favResult = bookRepo.getFavorites()
            val tbrResult = bookRepo.getToBeRead()

            favResult
                .onSuccess { favorites.addAll(it) }
                .onFailure { errorMessage.value = it.localizedMessage }

            tbrResult
                .onSuccess { toBeRead.addAll(it) }
                .onFailure { errorMessage.value = it.localizedMessage }

            isLoading.value = false
        }
    }

    fun addToFavorites(book: Book) {
        viewModelScope.launch {
            bookRepo.addFavorite(book)
            if (!favorites.any { it.title == book.title }) {
                favorites.add(book)
            }
        }
    }

    fun addToToBeRead(book: Book) {
        viewModelScope.launch {
            bookRepo.addToBeRead(book)
            if (!toBeRead.any { it.title == book.title }) {
                toBeRead.add(book)
            }
        }
    }

    fun removeFavorite(book: Book) {
        viewModelScope.launch {
            bookRepo.removeFavorite(book.title)
            favorites.removeAll { it.title == book.title }
        }
    }

    fun removeToBeRead(book: Book) {
        viewModelScope.launch {
            bookRepo.removeFromToBeRead(book.title)
            toBeRead.removeAll { it.title == book.title }
        }
    }

    fun isFavorite(title: String): Boolean {
        return favorites.any { it.title == title }
    }

    fun isToRead(title: String): Boolean {
        return toBeRead.any { it.title == title }
    }

}
