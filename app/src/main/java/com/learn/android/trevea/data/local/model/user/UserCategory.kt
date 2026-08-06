package com.learn.android.trevea.data.local.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_categories")
data class UserCategory(
    @PrimaryKey
    val categoryId: Int,
    val categoryName: String
)

