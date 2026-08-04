package com.learn.android.trevea.network.repository

import com.learn.android.trevea.model.otdb.CategoryResponse
import com.learn.android.trevea.network.service.OtdbApiService

class OtdbRepository(private val api: OtdbApiService) {

    suspend fun getAllCategories(): Result<CategoryResponse> {
        return try {
            val response = api.getCategory()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}