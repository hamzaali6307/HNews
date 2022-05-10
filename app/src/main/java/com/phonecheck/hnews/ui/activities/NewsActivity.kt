package com.phonecheck.hnews.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.phonecheck.hnews.R
import com.phonecheck.hnews.apiCall.localDb.ArticleDataBase
import com.phonecheck.hnews.databinding.ActivityNewsBinding
import com.phonecheck.hnews.ui.repositories.NewsRepository
import com.phonecheck.hnews.ui.viewModels.NewsViewModel
import com.phonecheck.hnews.utills.ViewModelFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this,
            ViewModelFactory(application,
                NewsRepository(ArticleDataBase.invoke(this))))[NewsViewModel::class.java]

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView.apply {
            setOnItemSelectedListener {
                navController.popBackStack()
                when (it.itemId) {
                    R.id.breakingNewsFrag -> {
                        navController.navigate(R.id.breakingNewsFrag)
                    }
                    R.id.savedNewsFrag -> {
                        navController.navigate(R.id.savedNewsFrag)
                    }
                    R.id.settingsFrag -> {
                        navController.navigate(R.id.settingsFrag)
                    }
                }
                true
            }
        }
    }

    override fun onBackPressed() {
        when {
            NavHostFragment.findNavController(navHostFragment).currentDestination?.id != R.id.breakingNewsFrag &&
                    NavHostFragment.findNavController(navHostFragment).currentDestination?.id != R.id.preferenceFrag
            -> {
                navController.navigate(R.id.breakingNewsFrag)
            }
            NavHostFragment.findNavController(navHostFragment).currentDestination?.id == R.id.preferenceFrag -> {
                navController.navigate(R.id.settingsFrag)
            }
            else -> {
                super.onBackPressed()
                finish()
            }
        }
    }
}