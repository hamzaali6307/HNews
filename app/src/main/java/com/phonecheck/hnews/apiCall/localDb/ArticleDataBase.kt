package com.phonecheck.hnews.apiCall.localDb

import android.content.Context
import androidx.room.*
import com.phonecheck.hnews.apiCall.localDb.dbUtills.Converters
import com.phonecheck.hnews.apiCall.localDb.interfaces.ArticleDao
import com.phonecheck.hnews.ui.models.Article
import com.phonecheck.hnews.utills.Constant.LOCAL_DB

@Database(entities = [Article::class],
    version = 1)

@TypeConverters(Converters::class)
abstract class ArticleDataBase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDataBase(context).also { instance = it }
        }

        private fun createDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDataBase::class.java,
            LOCAL_DB
        ).build()
    }
}