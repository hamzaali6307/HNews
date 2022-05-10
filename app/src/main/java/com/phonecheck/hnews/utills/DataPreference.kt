package com.phonecheck.hnews.utills

import android.content.Context

class DataPreference(context: Context) {
    private val file = "com.phonecheck.hnews"
    private val newsCategory = "news_category"
    private val language = "language"
    private val country = "country"
    private var sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setCategory(category: String) {
        editor.putString(newsCategory, category)
        editor.apply()
    }

    fun getCategory() : String {
        return sharedPreferences.getString(newsCategory, "")!!
    }

    fun setLanguage(lang: String) {
        editor.putString(language, lang)
        editor.apply()
    }

    fun getLanguage() : String {
        return sharedPreferences.getString(language, "en")!!
    }

    fun setCountry(count: String) {
        editor.putString(country, count)
        editor.apply()
    }

    fun getCountry() : String {
        return sharedPreferences.getString(country, "")!!
    }

}