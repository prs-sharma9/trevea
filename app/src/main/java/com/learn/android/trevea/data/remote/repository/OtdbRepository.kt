package com.learn.android.trevea.data.remote.repository

import android.util.Log
import com.learn.android.trevea.data.remote.model.otdb.CategoryResponse
import com.learn.android.trevea.data.remote.model.otdb.Question
import com.learn.android.trevea.data.remote.model.otdb.QuestionResponse
import com.learn.android.trevea.data.remote.service.OtdbApiService
import com.learn.android.trevea.utils.decodedHtml

class OtdbRepository(private val api: OtdbApiService) {

    private val tag = "Trevea: OtdbRepository"
    suspend fun getAllCategories(): Result<CategoryResponse> {
        return try {
            val response = api.getCategory()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuestions(
        amount: Int = 10,
        categoryId: Int = 0,
    ): Result<QuestionResponse> {
        return try {
            val response = api.getQuestions(
                amount = amount,
                category = categoryId
            )
            Log.d(tag, "ResponseCode: ${response.responseCode}")
            Log.d(tag, "Question Count: ${response.questionList.size}")

            when (response.responseCode) {
                0 -> {
                    // Success Response
                    val decodedQuestionList = response.questionList.map {
                        it.copy(
                            question = it.question.decodedHtml(),
                            correctAnswer = it.correctAnswer.decodedHtml(),
                            incorrectAnswers = it.incorrectAnswers.map { a -> a.decodedHtml() }
                        )
                    }
                    val decodedResponse = QuestionResponse(response.responseCode, decodedQuestionList)
                    Result.success(decodedResponse)

                }

                1 -> {
                    // No Results Could not return results.
                    // The API doesn't have enough questions for your query.
                    // (Ex. Asking for 50 Questions in a Category that only has 20.)
                    Result.failure<Exception>(
                        Exception("Not Enough Question, Response code: ${response.responseCode}")
                    )
                }

                2 -> {
                    // Invalid Parameter Contains an invalid parameter.
                    // Arguements passed in aren't valid. (Ex. Amount = Five)
                    Result.failure<Exception>(
                        Exception("Invalid Parameters, Response code: ${response.responseCode}")
                    )
                }

                3 -> {
                    // Token Not Found Session Token does not exist.
                    Result.failure<Exception>(
                        Exception("Invalid Token, Response code: ${response.responseCode}")
                    )
                }

                4 -> {
                    // Token Empty Session Token has returned all possible questions for the specified query.
                    // Resetting the Token is necessary.
                    Result.failure<Exception>(
                        Exception("Token refresh required, Response code: ${response.responseCode}")
                    )
                }

                5 -> {
                    // Rate Limit Too many requests have occurred.
                    // Each IP can only access the API once every 5 seconds.
                    Result.failure<Exception>(
                        Exception("Too many request, Response code: ${response.responseCode}")
                    )
                }

                else -> {
                    // unknown error
                    Result.failure<Exception>(
                        Exception("Unknown error, Response code: ${response.responseCode}")
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        } as Result<QuestionResponse>
    }
}