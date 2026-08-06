package com.learn.android.trevea.data.remote.model.otdb

import com.google.gson.annotations.SerializedName

//{
//  "trivia_categories":
//      [
//          {
//              "id":9,
//              "name":"General Knowledge"
//          },
//          {
//              "id":10,
//              "name":"Entertainment: Books"
//          }
//      ]
//}

data class CategoryResponse(
//    val responseCode: Int,

    @SerializedName("trivia_categories")
    val triviaCategories: List<Category>
)

data class Category (
    @SerializedName("id")
    val categoryId: Int,
    @SerializedName("name")
    val categoryName: String
)