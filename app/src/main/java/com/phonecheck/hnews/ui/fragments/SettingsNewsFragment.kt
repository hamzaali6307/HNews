package com.phonecheck.hnews.ui.fragments

import android.content.Intent
import android.content.Intent.EXTRA_SUBJECT
import android.net.Uri
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
        initMultipleViewsClickListener(tvNewsCategory,tvLanguage,tvCountry,tvBugSpot,tvSuggestion)
    }

    override fun onClick(view: View?) {
        when (view) {
            tvNewsCategory,tvLanguage, tvCountry  -> {
                findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                    Bundle().apply {
                        putString(Constant.BUNDLE_TITLE, when(view){
                            tvNewsCategory -> getString(R.string.category)
                            tvLanguage -> getString(R.string.language)
                            tvCountry -> getString(R.string.country)
                            else -> { "" }
                        } )
                    })
            }
            tvSuggestion ->  {
                startActivity(Intent.createChooser(  Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:hamza.ali6307@gmail.com")
                    putExtra(EXTRA_SUBJECT,getString(R.string.i_have_a_suggestion))
                }, getString(R.string.i_have_a_suggestion)))
            }
            tvBugSpot ->  {
                startActivity(Intent.createChooser(  Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:hamza.ali6307@gmail.com")
                    putExtra(EXTRA_SUBJECT,getString(R.string.i_spotted_a_bug))
                }, getString(R.string.i_spotted_a_bug)))
            }
        }
    }
}