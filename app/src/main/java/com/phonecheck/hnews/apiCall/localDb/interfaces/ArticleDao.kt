package com.phonecheck.hnews.apiCall.localDb.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.phonecheck.hnews.ui.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles() :LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}