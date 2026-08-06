package com.learn.android.trevea.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.data.remote.model.otdb.Category

@Composable
fun CateogryList(
    modifier: Modifier = Modifier,
    categories: List<Category>,
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
            Log.d("MyTag", "1. CateogryList: $categories")
            categories.forEach { CategoryListItem(category = it) }
        }
    }

}

@Composable
fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )

    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                textAlign = TextAlign.Start,
                text = category.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }

}