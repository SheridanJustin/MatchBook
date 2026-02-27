package week11.st221773.matchbook.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleBooksClient {
    val api: GoogleBooksApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }
}
