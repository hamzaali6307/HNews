package com.phonecheck.hnews.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phonecheck.hnews.R
import com.phonecheck.hnews.ui.models.Article
import com.phonecheck.hnews.ui.models.NewsResponse
import com.phonecheck.hnews.ui.repositories.NewsRepository
import com.phonecheck.hnews.utills.MyApplication
import com.phonecheck.hnews.utills.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository,
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var pageNumber = 1
    var breakingNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us","en","")
    }

    fun getBreakingNews(countryCode: String,language :String,category: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode,language,category)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                pageNumber++
                breakingNewsResponse?.articles?.addAll(newsResponse.articles)
                return Resource.Success(breakingNewsResponse ?: newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                pageNumber++
                breakingNewsResponse?.articles?.addAll(newsResponse.articles)
                return Resource.Success(breakingNewsResponse ?: newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun savedArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedArticleLocalDb() = newsRepository.getSavedArticles()

    private suspend fun safeBreakingNewsCall(countryCode: String,language: String,category: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnected()) {
                breakingNews.postValue(handleNewsResponse(newsRepository.getBreakingNews(countryCode = countryCode,
                    language = language,
                    category = category,
                    pageNumber = pageNumber)))
            } else {
                breakingNews.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.internet_not_connected)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(getApplication<MyApplication>().getString(
                    R.string.network_failure_error)))
                else -> breakingNews.postValue(Resource.Error(t.message.toString()))
            }
        }

    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnected()) {
                breakingNews.postValue(handleSearchNewsResponse(newsRepository.searchNews(searchQuery = searchQuery,
                    pageNumber = pageNumber)))
            } else {
                breakingNews.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.internet_not_connected)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(getApplication<MyApplication>().getString(
                    R.string.network_failure_error)))
                else -> breakingNews.postValue(Resource.Error(t.message.toString()))
            }
        }

    }

    private fun hasInternetConnected(): Boolean {

        val connectivityManager =
            getApplication<MyApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(TRANSPORT_ETHERNET)
                -> true
                else -> {
                    false
                }
            }
        } else { // necessary for lower version devices
            connectivityManager.activeNetworkInfo?.runCatching {
                return when (type) {
                    TYPE_WIFI, TYPE_MOBILE, TYPE_ETHERNET -> true
                    else -> {
                        false
                    }
                }
            }
        }
        return false
    }
}