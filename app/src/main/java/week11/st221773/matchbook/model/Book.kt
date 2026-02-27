package week11.st221773.matchbook.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val title: String,
    val author: String,
    val reason: String,
    val coverUrl: String? = null,
    val description: String? = null,
    val categories: List<String>? = null,
    val previewLink: String? = null
)
