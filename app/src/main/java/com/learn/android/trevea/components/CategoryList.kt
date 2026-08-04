package com.learn.android.trevea.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.learn.android.trevea.model.otdb.Category
import com.learn.android.trevea.viewmodel.TreveaViewModel

@Composable
fun CateogryList(
    modifier: Modifier = Modifier,
    categories: List<Category>,
) {
    Surface(
        modifier = modifier
    ) {
        Log.d("MyTag", "1. CateogryList: $categories")
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Log.d("MyTag", "2. CateogryList: $categories")
            items(
                items = categories
            ) {
                CategoryListItem(category = it)
            }
        }
    }
}

@Composable
fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category
) {
    Log.d("MyTag", "CategoryListItem: $category")
    Text(text = category.categoryName)
}