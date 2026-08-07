package com.learn.android.trevea.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learn.android.trevea.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCategoryDao {

    @Query("SELECT * FROM user_categories ORDER BY categoryId ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT COUNT(*) FROM user_categories WHERE categoryId = :categoryId")
    suspend fun isCategorySelected(categoryId: Int): Int // return 0 or 1
}