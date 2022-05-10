package com.phonecheck.hnews.utills


object Constant {
    const val URL_BASE = "https://newsapi.org"
    const val URL_SEARCHING = "v2/everything"
    const val URL_TOP_HEADLINES = "v2/top-headlines"
    const val API_KEY = "8f0762f66f3f476da2a8708e81d5108a"
    const val LOCAL_DB = "article_db.db"
    const val SEARCHING_DELAY = 2000L
    const val QUERY_ITEM_LENGTH = 20
    const val BUNDLE_TITLE = "article"
    var isScrolling = false
    var isPageLoading = false
    var isLastPage = false


}