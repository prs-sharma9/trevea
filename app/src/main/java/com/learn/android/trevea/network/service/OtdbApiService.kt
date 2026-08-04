package com.learn.android.trevea.network.service

import com.learn.android.trevea.model.otdb.CategoryResponse
import retrofit2.http.GET

interface OtdbApiService {
    @GET("api_category.php")
    suspend fun getCategory(): CategoryResponse
}