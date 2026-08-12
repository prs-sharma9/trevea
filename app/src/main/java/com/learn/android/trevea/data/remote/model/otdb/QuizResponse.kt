package com.learn.android.trevea.data.remote.model.otdb

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.internal.NamedCompanion

data class Question (
    @SerializedName("type")
    val type: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
) {
    companion object {
        fun default() = Question(
            type = "multiple",
            difficulty = "easy",
            category = "General Knowledge",
            question = "What is the full meaning of RAM?",
            correctAnswer = "Random Access Memory",
            incorrectAnswers = listOf(
                "Random Assist Memory",
                "Rand Assist Mandate",
                "Ram"
            )
        )
    }
}

data class QuestionResponse (
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val questionList: List<Question>
)