package com.phonecheck.hnews.ui.repositories

import com.phonecheck.hnews.apiCall.RetroInstance
import com.phonecheck.hnews.apiCall.localDb.ArticleDataBase
import com.phonecheck.hnews.ui.models.Article

class NewsRepository(private val db: ArticleDataBase) {

    suspend fun getBreakingNews(countryCode: String ,language: String ,category: String,pageNumber: Int) =
        RetroInstance.api.getBreakingNews(countryCode = countryCode, language = language, category = category,pageNumber =  pageNumber)

    suspend fun  searchNews(searchQuery:String, pageNumber: Int) =
        RetroInstance.api.searchNews(searchQuery = searchQuery, pageNumber = pageNumber)

    suspend fun upsertArticle(article: Article) = db.articleDao().upsert(article)

    fun getSavedArticles() = db.articleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)
}