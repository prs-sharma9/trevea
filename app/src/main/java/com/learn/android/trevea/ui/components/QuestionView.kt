package com.learn.android.trevea.ui.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.R
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun QuestionView(
    type: String,
    question: String,
    correctAnswer: String,
    incorrectAnswer: List<String>,
    streak: Int,
    isOptionClickable: Boolean,
    onAnswerSelected: (Boolean) -> Unit
) {

    val tag = "Trevea: QuestionView"

    Log.d(tag, "isOptionClickable: $isOptionClickable")

    val questionId = remember (question) {
        Random.nextInt()
    }

    val answers = remember (questionId) {
        val list = incorrectAnswer.toMutableList()
        val correctAnsIdx = Random(System.currentTimeMillis()).nextInt(until = incorrectAnswer.size+1)
        list.add(correctAnsIdx, correctAnswer)
        list
    }

    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {

                Surface(
                    modifier = Modifier
                        .weight(0.4f),
                    color = Color.Transparent
                ) {
                    QuestionView(
                        questionTxt = question
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(0.6f),
                    color = Color.Transparent
                ) {
                    OptionsView(
                        answers = answers,
                        questionId = questionId,
                        correctAnswer = correctAnswer,
                        isOptionClickable = isOptionClickable,
//                optClickable = optClickable,
                        onOptionSelected = onAnswerSelected
                    )
                }
            }
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row (
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .weight(0.5f),
                    color = Color.Transparent
                ) {
                    QuestionView(
                        questionTxt = question
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(0.5f),
                    color = Color.Transparent
                ) {
                    OptionsView(
                        answers = answers,
                        questionId = questionId,
                        correctAnswer = correctAnswer,
                        isOptionClickable = isOptionClickable,
                        onOptionSelected = onAnswerSelected
                    )
                }
            }
        }
    }
}


@Composable
fun OptionsView(
    modifier: Modifier = Modifier,
    answers: List<String>,
    questionId: Int,
    correctAnswer: String,
    isOptionClickable: Boolean,
    onOptionSelected: (Boolean) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(
            items = answers
        ) { option ->

            OptionView(
                optionTxt = option,
                questionId = questionId.absoluteValue,
                isCorrect = option == correctAnswer,
                isClickable = isOptionClickable
            ) {
                onOptionSelected(option == correctAnswer)
            }
        }
    }
}


@Composable
fun QuestionView(
    modifier: Modifier = Modifier,
    questionTxt: String
) {
    Column(
        modifier = modifier
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = questionTxt,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(R.color.secondary)
        )
    }
}

@Composable
fun OptionView (
    optionTxt: String,
    isCorrect:Boolean,
    questionId: Int,
    isClickable: Boolean,
    cardClick: () -> Unit
) {
    var isClicked by remember (questionId) { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = if (isClicked && isCorrect)
                colorResource(R.color.option_bg_correct)
            else
                if (!isCorrect && isClicked)
                    colorResource(R.color.option_bg_wrong)
                else colorResource(R.color.option_bg_neutral)
        ),
        onClick = {
            if(!isClicked && isClickable) {
                Log.d("QuestionView", "isCorrect: $isCorrect")
                isClicked = true
                cardClick()
            }
        },
        border = BorderStroke(1.dp, colorResource(R.color.secondary))
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 10.dp),
            text = optionTxt,
            style = MaterialTheme.typography.labelMedium,
            color = if (isClicked && isCorrect)
                colorResource(R.color.option_correct)
            else
                if (!isCorrect && isClicked)
                    colorResource(R.color.option_wrong)
                else colorResource(R.color.accent)
        )
    }
}