package com.phonecheck.hnews.ui.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phonecheck.hnews.R
import com.phonecheck.hnews.ui.activities.NewsActivity
import com.phonecheck.hnews.ui.adapters.NewsAdapter
import com.phonecheck.hnews.ui.viewModels.NewsViewModel
import com.phonecheck.hnews.utills.Constant.BUNDLE_TITLE
import com.phonecheck.hnews.utills.isVisible
import com.phonecheck.hnews.utills.showSnackBar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.item_error_view.*

class SavedNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        initViews()
        viewsListner()

        lifecycleScope.launchWhenCreated {
            viewModel.apply {
                getSavedArticleLocalDb().observe(viewLifecycleOwner) { article ->
                    when {
                        article.isNotEmpty() -> {
                            viewLoadingData(true)
                            newsAdapter.differ.submitList(article)
                        }
                        else -> {
                            viewLoadingData(false)
                        }
                    }
                }
            }
        }
    }

    private fun viewsListner() {

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteArticle(newsAdapter.differ.currentList[viewHolder.adapterPosition])
                showSnackBar(view!!, getString(R.string.delete_article))
            }
        }
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(rvBreakingNews)
        }    }

    private fun initViews() {
        et_searching.isVisible(false)

        viewModel = (activity as NewsActivity).viewModel

    }

    private fun viewLoadingData(show: Boolean) {
        when {
            show -> {
                clError.isVisible(!show)
                ll_loading.isVisible(!show)
                rvBreakingNews.isVisible(show)
            }
            else -> {
                clError.isVisible(!show)
                ll_loading.isVisible(show)
                rvBreakingNews.isVisible(show)
            }
        }
    }

    private fun setUpAdapter() {
        newsAdapter = NewsAdapter {
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable(BUNDLE_TITLE, it)
                })

        }
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = if(resources.configuration.orientation ==  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                LinearLayoutManager(requireActivity())
            }else{
                GridLayoutManager(requireContext(),2)
            }
        }
    }
}