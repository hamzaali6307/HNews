package com.phonecheck.hnews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.phonecheck.hnews.R
import com.phonecheck.hnews.utills.Constant
import com.phonecheck.hnews.utills.initMultipleViewsClickListener
import kotlinx.android.synthetic.main.fragment_settings_news.*

class SettingsNewsFragment : Fragment(R.layout.fragment_settings_news), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {
        initMultipleViewsClickListener(tvNewsCategory,tvLanguage,tvCountry)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvNewsCategory, R.id.tvLanguage,  R.id.tvCountry  -> {
                findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                    Bundle().apply {
                        putString(Constant.BUNDLE_TITLE, when(view.id){
                            R.id.tvNewsCategory -> getString(R.string.category)
                            R.id.tvLanguage -> getString(R.string.language)
                            R.id.tvCountry -> getString(R.string.country)
                            else -> { "" }
                        } )
                    })
            }
        }
    }
}