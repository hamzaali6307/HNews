package com.phonecheck.hnews.utills

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phonecheck.hnews.ui.repositories.NewsRepository
import com.phonecheck.hnews.ui.viewModels.NewsViewModel

class ViewModelFactory(private val application: Application ,private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(application,newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

