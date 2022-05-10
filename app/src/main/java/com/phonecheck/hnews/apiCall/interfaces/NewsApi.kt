package com.phonecheck.hnews.apiCall.interfaces

import com.phonecheck.hnews.ui.models.NewsResponse
import com.phonecheck.hnews.utills.Constant.API_KEY
import com.phonecheck.hnews.utills.Constant.URL_SEARCHING
import com.phonecheck.hnews.utills.Constant.URL_TOP_HEADLINES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(URL_TOP_HEADLINES)
    suspend fun getBreakingNews(
        @Query("category")
        category :String = "",
        @Query("language")
        language :String = "en",
        @Query("country")
        countryCode :String = "US",
        @Query("page")
        pageNumber :Int  = 1,
        @Query("apiKey")
        apiKey :String = API_KEY
    ) : Response<NewsResponse>


    @GET(URL_SEARCHING)
    suspend fun searchNews(
        @Query("q")
        searchQuery :String ,
        @Query("page")
        pageNumber :Int  = 1,
        @Query("apiKey")
        apiKey :String = API_KEY
    ) : Response<NewsResponse>
}