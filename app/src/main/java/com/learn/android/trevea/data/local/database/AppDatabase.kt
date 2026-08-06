package com.learn.android.trevea.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learn.android.trevea.data.local.dao.UserCategoryDao
import com.learn.android.trevea.data.local.model.user.UserCategory

@Database(entities = [UserCategory::class], version = 1, exportSchema = false)
abstract class TreveaDatabase : RoomDatabase() {

    abstract fun userCategoryDao(): UserCategoryDao

    companion object {
        private const val DATABASE_NAME = "trevea_database"

        @Volatile
        private var DB_INSTANCE: TreveaDatabase? = null

        fun getInstance(context: Context): TreveaDatabase {
            return DB_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    name = DATABASE_NAME,
                    klass = TreveaDatabase::class.java
                ).build()
                DB_INSTANCE = instance
                instance
            }
        }
    }
}