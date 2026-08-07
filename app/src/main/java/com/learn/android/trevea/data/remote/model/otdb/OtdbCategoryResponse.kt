package com.learn.android.trevea.data.remote.model.otdb

import com.google.gson.annotations.SerializedName
import com.learn.android.trevea.data.model.Category

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

    @SerializedName("trivia_categories")
    val triviaCategories: List<Category>
)