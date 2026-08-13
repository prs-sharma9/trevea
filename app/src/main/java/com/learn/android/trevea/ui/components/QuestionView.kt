package com.learn.android.trevea.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

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

    val tag = "QuestionView"

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

    var optClickable by remember (questionId) { mutableStateOf(true) }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.3f)
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

                    OptionView(
                        optionTxt = option,
                        questionId = questionId.absoluteValue,
                        isCorrect = option == correctAnswer,
                        isClickable = isOptionClickable
                    ) {
                        if(optClickable) {
                            optClickable = false
                        }
                        onAnswerSelected(option == correctAnswer)
                    }
                }
            }
        }
        Surface(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth(),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "$streak",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Icon(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak Icon",
                        tint = colorResource(R.color.streak_flame)
                    )
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
    isClickable: Boolean,
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
            if(!isClicked && isClickable) {
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
            style = MaterialTheme.typography.labelMedium,
            color = if (isClicked && isCorrect)
                colorResource(R.color.option_correct)
            else
                if (!isCorrect && isClicked)
                    colorResource(R.color.option_wrong)
                else colorResource(R.color.option_neutral)
        )
    }
}