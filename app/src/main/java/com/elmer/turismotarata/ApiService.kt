package com.elmer.turismotarata.data

import com.elmer.turismotarata.model.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("api_lugares.php")
    suspend fun getPlaces(): List<Place>
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.38/api_tarata/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}