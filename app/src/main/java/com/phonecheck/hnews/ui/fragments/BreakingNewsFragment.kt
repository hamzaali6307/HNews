package com.phonecheck.hnews.ui.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phonecheck.hnews.R
import com.phonecheck.hnews.ui.activities.NewsActivity
import com.phonecheck.hnews.ui.adapters.NewsAdapter
import com.phonecheck.hnews.ui.models.Article
import com.phonecheck.hnews.ui.viewModels.NewsViewModel
import com.phonecheck.hnews.utills.Constant.BUNDLE_TITLE
import com.phonecheck.hnews.utills.Constant.QUERY_ITEM_LENGTH
import com.phonecheck.hnews.utills.Constant.SEARCHING_DELAY
import com.phonecheck.hnews.utills.Constant.isLastPage
import com.phonecheck.hnews.utills.Constant.isPageLoading
import com.phonecheck.hnews.utills.Constant.isScrolling
import com.phonecheck.hnews.utills.DataPreference
import com.phonecheck.hnews.utills.Resource
import com.phonecheck.hnews.utills.isVisible
import com.phonecheck.hnews.utills.showSnackBar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.item_error_view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var newsAdapter: NewsAdapter
    private var newsList: ArrayList<Article> = arrayListOf()
    private var job: Job? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var prefSession: DataPreference

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            val linearLayout = (rvBreakingNews.layoutManager as LinearLayoutManager)
            val firstVisibleItemPosition = linearLayout.findFirstVisibleItemPosition()
            val visibleItemCount = linearLayout.childCount
            val totalItemCount = linearLayout.itemCount

            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThenVisible = totalItemCount >= QUERY_ITEM_LENGTH
            val isNotLoadingAndNotLastPage = !isPageLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val shouldPaginate = isNotAtBeginning && isTotalMoreThenVisible &&
                    isNotLoadingAndNotLastPage && isAtLastItem && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews(prefSession.getCountry(),
                    prefSession.getLanguage(),
                    prefSession.getCategory())
                isScrolling = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrolling = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

        initViews()

        viewsListner()

        loadDataFromNetwork()
    }

    private fun viewsListner() {
        et_searching.doAfterTextChanged { editable ->
            newsList = arrayListOf()
            job?.cancel()
            job = MainScope().launch {

                delay(SEARCHING_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    } else {
                        viewModel.pageNumber = 1
                        viewModel.getBreakingNews(prefSession.getCountry(),
                            prefSession.getLanguage(),
                            prefSession.getCategory())
                    }
                }
            }
        }
    }

    private fun loadDataFromNetwork() { // avoid warning
        lifecycleScope.launchWhenCreated {
            viewModel.apply {
                breakingNews.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Success -> {
                            viewLoadingData(show = true)
                            response.data?.let { newsResponse ->
                                if (newsResponse.totalResults != 0) {
                                    for (item in newsResponse.articles) {
                                        newsList.add(item)
                                    }
                                }
                                newsAdapter.differ.submitList(newsList)
                                isLastPage =
                                    viewModel.pageNumber == newsResponse.totalResults / QUERY_ITEM_LENGTH + 2
                                if (isLastPage) {
                                    rvBreakingNews.setPadding(0, 0, 0, 0)
                                }
                            }
                        }
                        is Resource.Error -> {
                            if (newsList.size == 0) {
                                viewLoadingData(false)
                                response.message?.let { message ->
                                    showSnackBar(clMainView, message)
                                }
                            }
                        }
                        is Resource.Loading -> {
                            clError.isVisible(false)
                            ll_loading.isVisible(true)
                        }
                    }
                }
            }
        }
    }

    private fun initViews() {
        viewModel = (activity as NewsActivity).viewModel
        prefSession = DataPreference(requireActivity())
    }

    private fun viewLoadingData(show: Boolean) {
        when {
            show -> {
                isPageLoading = false
                clError.isVisible(!show)
                ll_loading.isVisible(!show)
                rvBreakingNews.isVisible(show)
            }
            else -> {
                isPageLoading = true
                clError.isVisible(!show)
                ll_loading.isVisible(show)
                rvBreakingNews.isVisible(show)
            }
        }
    }

    private fun setUpAdapter() {
        newsAdapter = NewsAdapter {
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable(BUNDLE_TITLE, it)
                })

        }
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager =
                if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(requireActivity())
                } else {
                    GridLayoutManager(requireContext(), 2)
                }
            addOnScrollListener(onScrollListener)
        }
    }
}