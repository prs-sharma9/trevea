package com.learn.android.trevea.data.local.repository

import com.learn.android.trevea.data.model.Category
import com.learn.android.trevea.data.local.dao.UserCategoryDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userCategoryDao: UserCategoryDao) {

    val userCategoryList: Flow<List<Category>> = userCategoryDao.getAllCategories()

    suspend fun insertUserCategory(category: Category) {
        userCategoryDao.insertCategory(category)
    }

    suspend fun deleteUserCategory(category: Category) {
        userCategoryDao.deleteCategory(category)
    }

    suspend fun isCategorySelected(categoryId: Int): Boolean {
        return userCategoryDao.isCategorySelected(categoryId) == 1
    }
}