package com.learn.android.trevea.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_categories")
data class Category(
    @PrimaryKey
    @SerializedName("id")
    val categoryId: Int,
    @SerializedName("name")
    val categoryName: String,
)