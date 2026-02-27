package week11.st221773.matchbook.data.repository

import week11.st221773.matchbook.network.GoogleBooksClient
import week11.st221773.matchbook.model.Book

class GoogleBooksRepository {

    suspend fun enrichBook(book: Book): Book {
        return try {
            val query = "intitle:${book.title}+inauthor:${book.author}"
            val response = GoogleBooksClient.api.searchBooks(query)

            val volume = response.items?.firstOrNull()?.volumeInfo
                ?: return book  // no data found

            val cover = volume.imageLinks?.thumbnail?.replace("http", "https")

            book.copy(
                coverUrl = cover,
                description = volume.description ?: book.reason,
                categories = volume.categories,
                previewLink = volume.previewLink
            )

        } catch (e: Exception) {
            book
        }
    }
}
