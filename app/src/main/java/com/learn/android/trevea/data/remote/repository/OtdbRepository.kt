package com.learn.android.trevea.data.remote.repository

import android.util.Log
import com.learn.android.trevea.data.remote.model.otdb.CategoryResponse
import com.learn.android.trevea.data.remote.model.otdb.Question
import com.learn.android.trevea.data.remote.model.otdb.QuestionResponse
import com.learn.android.trevea.data.remote.service.OtdbApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OtdbRepository(private val api: OtdbApiService) {

    suspend fun getAllCategories(): Result<CategoryResponse> {
        return try {
            val response = api.getCategory()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun getQuestions(
//        amount: Int = 10
//    ): Result<QuestionResponse> {
//        return try {
//            val response = api.getQuestions(amount = amount)
//            Result.success(response)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

    suspend fun getQuestions(
        amount: Int = 10,
        categoryId: Int = 0,
    ): Result<QuestionResponse> {
        return try {
            val response = api.getQuestions(
                amount = amount,
                category = categoryId
            )
            Log.d("MyTag", "ResponseCode: ${response.responseCode.toString()}")
            Log.d("MyTag", "Question Count: ${response.questionList.size.toString()}")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun getContinuousQuestion_old(
//        amount: Int = 10,
//        categoryId: Int = 0
//    ): Flow<Result<List<Question>>> = flow {
//        while (true) {
//            val result = try {
//                val response = api.getQuestions( amount = amount, category = categoryId)
//                if(response.responseCode == 0) {
//                    Result.success(response.questionList)
//                } else {
//                    Result.failure(Exception("ResponseCode: ${response.responseCode}"))
//                }
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//
//            emit(result)
//            delay(10_000) // 10 milli second delay
//        }
//    }.flowOn(Dispatchers.IO)

    suspend fun getContinuousQuestion(
        amount: Int = 10,
        categoryId: Int = 0
    ): Flow<Result<List<Question>>> = flow {
        while (true) {
            val result = try {
                val response = api.getQuestions( amount = amount, category = categoryId)
                if(response.responseCode == 0) {
                    Result.success(response.questionList)
                } else {
                    Result.failure(Exception("ResponseCode: ${response.responseCode}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

            emit(result)
            delay(10_000) // 10 milli second delay
        }
    }.flowOn(Dispatchers.IO)
}