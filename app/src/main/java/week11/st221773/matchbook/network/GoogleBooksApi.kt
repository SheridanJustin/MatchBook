package week11.st221773.matchbook.network

import retrofit2.http.GET
import retrofit2.http.Query
import week11.st221773.matchbook.model.google.GoogleBooksResponse

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 1
    ): GoogleBooksResponse
}
