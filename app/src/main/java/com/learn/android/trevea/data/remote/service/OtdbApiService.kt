package com.learn.android.trevea.data.remote.service

import com.learn.android.trevea.data.remote.model.otdb.CategoryResponse
import retrofit2.http.GET

interface OtdbApiService {
    @GET("api_category.php")
    suspend fun getCategory(): CategoryResponse
}