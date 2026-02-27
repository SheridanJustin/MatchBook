package week11.st221773.matchbook.model

import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val books: List<Book>
)
