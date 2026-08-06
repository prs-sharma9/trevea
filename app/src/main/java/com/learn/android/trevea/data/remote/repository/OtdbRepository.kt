package com.learn.android.trevea.data.remote.repository

import com.learn.android.trevea.data.remote.model.otdb.CategoryResponse
import com.learn.android.trevea.data.remote.service.OtdbApiService

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