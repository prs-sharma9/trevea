package com.learn.android.trevea.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.R
import com.learn.android.trevea.data.model.Category

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    allCategories: List<Category>,
    onItemClick: (Category) -> Unit = {}

) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(
                state = rememberScrollState()
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        FlowRow (
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Log.d("MyTag", "1. CategoryList: $allCategories")
            allCategories.forEach {
                CategoryListItem(
                    category = it,
                    onClick = {
                        onItemClick(it)
                    }
                )
            }
        }
    }

}

@Composable
fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category = Category(categoryId = 0, categoryName = "Movies"),
    onClick: () -> Unit = {},

    ) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.secondary)
        ),
        onClick = {
            onClick()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                textAlign = TextAlign.Start,
                text = category.categoryName,
                style = MaterialTheme.typography.displaySmall,
                color = colorResource(R.color.accent)
            )
        }
    }
}