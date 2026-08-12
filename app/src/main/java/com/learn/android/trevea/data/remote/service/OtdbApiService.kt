package com.learn.android.trevea.data.remote.service

import com.learn.android.trevea.data.remote.model.otdb.CategoryResponse
import com.learn.android.trevea.data.remote.model.otdb.QuestionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OtdbApiService {
    @GET("api_category.php")
    suspend fun getCategory(): CategoryResponse

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int = 0
    ): QuestionResponse
}