package com.learn.android.trevea.data.local.repository

import com.learn.android.trevea.data.local.dao.UserCategoryDao
import com.learn.android.trevea.data.local.model.user.UserCategory
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userCategoryDao: UserCategoryDao) {

    val userCategoryList: Flow<List<UserCategory>> = userCategoryDao.getAllCategories()

    suspend fun insertUserCategory(category: UserCategory) {
        userCategoryDao.insertCategory(category)
    }

    suspend fun deleteUserCategory(category: UserCategory) {
        userCategoryDao.deleteCategory(category)
    }

    suspend fun isCategorySelected(categoryId: Int): Boolean {
        return userCategoryDao.isCategorySelected(categoryId) == 1
    }
}