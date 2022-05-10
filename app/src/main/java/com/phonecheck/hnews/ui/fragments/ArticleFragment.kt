package com.phonecheck.hnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.phonecheck.hnews.R
import com.phonecheck.hnews.ui.activities.NewsActivity
import com.phonecheck.hnews.ui.viewModels.NewsViewModel
import com.phonecheck.hnews.utills.isVisible
import com.phonecheck.hnews.utills.showSnackBar
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.item_error_view.*

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var viewModel: NewsViewModel
    private val argument by navArgs<ArticleFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        webView.apply {
            webViewClient = WebViewClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    ll_loading?.isVisible(false)
                    clError?.isVisible(false)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?,
                ) {
                    super.onReceivedError(view, request, error)
                    clError?.isVisible(true)
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?,
                ) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    clError?.isVisible(true)
                }
            }

            argument.article.url?.let { loadUrl(it) }
        }

        fab.setOnClickListener {
            viewModel.savedArticle(argument.article)
            showSnackBar(webView, getString(R.string.saved_article))
        }
    }
}