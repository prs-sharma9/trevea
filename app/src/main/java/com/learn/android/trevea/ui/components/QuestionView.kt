package com.learn.android.trevea.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.R
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun QuestionView(
    type: String,
    question: String,
    correctAnswer: String,
    incorrectAnswer: List<String>,
    onAnswerSelected: () -> Unit
) {

    val questionId = remember (question) {
        Random.nextInt()
    }

    val answers = remember (questionId) {
        val list = incorrectAnswer.toMutableList()
        val correctAnsIdx = Random(System.currentTimeMillis()).nextInt(until = incorrectAnswer.size+1)
        list.add(correctAnsIdx, correctAnswer)
        list
    }



    var optClickable by remember (questionId) { mutableStateOf(true) }



    LaunchedEffect(optClickable) {
        if(!optClickable) {
            Log.d("QuestionView", "LaunchedEffect: Getting next question")
            delay(2000)
            onAnswerSelected()
        }

    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Column (
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                items(
                    items = answers
                ) { option ->

//                    var optionBgColorState by remember { mutableStateOf(OptionBgState.NEUTRAL) }

                    OptionView(
                        optionTxt = option,
                        questionId = questionId.absoluteValue,
                        isCorrect = option == correctAnswer
                    ) {
                        if(optClickable) {
                            optClickable = false
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun OptionView (
    optionTxt: String,
    isCorrect:Boolean,
    questionId: Int,
    cardClick: () -> Unit
) {
    var isClicked by remember (questionId) { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = if (isClicked && isCorrect)
                colorResource(R.color.option_bg_correct)
            else
                if (!isCorrect && isClicked)
                    colorResource(R.color.option_bg_wrong)
                else colorResource(R.color.option_bg_neutral)
        ),
        onClick = {
            if(!isClicked) {
                Log.d("QuestionView", "isCorrect: $isCorrect")
                isClicked = true
                cardClick()
            }


        }
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 5.dp),
            text = optionTxt,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


//enum class OptionBgState{
//    NEUTRAL, CORRECT, WRONG
//}