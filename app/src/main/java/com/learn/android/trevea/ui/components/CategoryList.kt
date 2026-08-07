package com.learn.android.trevea.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.data.model.Category

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    allCategories: List<Category>,
    userCategories: List<Category>,
    selectionMode: Boolean = false,
    onSelectionChanged: (Category) -> Unit = {}

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
                var isSelected by remember { mutableStateOf(userCategories.contains(it)) }
                CategoryListItem(
                    category = it,
                    enableSelection = selectionMode,
                    isSelected = isSelected,
                    onSelectionChanged = {
                        isSelected = !isSelected
                        onSelectionChanged(it)
                    }
                )
            }
        }
    }

}

@Composable
fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category,
    enableSelection: Boolean = true,
    onSelectionChanged: () -> Unit = {},
    isSelected: Boolean = true
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = {
            onSelectionChanged()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp)
        ) {
            if (enableSelection) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onSelectionChanged() }
                )
            }

            Text(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        bottom = 10.dp,
                        start = if (enableSelection) 0.dp else 10.dp,
                        end = 10.dp
                    ),
                textAlign = TextAlign.Start,
                text = category.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}