package week11.st221773.matchbook.network

import retrofit2.http.Body
import retrofit2.http.POST
import week11.st221773.matchbook.model.BookResponse

interface ApiService {

    @POST("/recommend")
    suspend fun getRecommendations(
        @Body request: Map<String, String>
    ): BookResponse
}
