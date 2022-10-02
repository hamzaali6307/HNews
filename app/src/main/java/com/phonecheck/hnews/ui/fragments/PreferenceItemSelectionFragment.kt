package com.phonecheck.hnews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phonecheck.hnews.R
import com.phonecheck.hnews.ui.adapters.PreferencesAdapter
import com.phonecheck.hnews.ui.models.Preference
import com.phonecheck.hnews.utills.Constant.BUNDLE_TITLE
import com.phonecheck.hnews.utills.DataPreference
import com.phonecheck.hnews.utills.showSnackBar
import kotlinx.android.synthetic.main.fragment_perference_item_selection.*

class PreferenceItemSelectionFragment : Fragment(R.layout.fragment_perference_item_selection) {
    private lateinit var prefAdapter: PreferencesAdapter
    private lateinit var prefSession: DataPreference
    private var arrayList: ArrayList<Preference> = arrayListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initIntent()
        iv_back.setOnClickListener {
            findNavController().navigate(R.id.settingsFrag)
        }
    }

    private fun initRecyclerView() {
        prefSession = DataPreference(requireActivity())
        prefAdapter = PreferencesAdapter {
            showSnackBar(rvPref, it.prefName)
            savePreference(it.details!!)
            findNavController().navigate(R.id.breakingNewsFrag)

        }
        rvPref.apply {
            adapter = prefAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun savePreference(prefName: String) {
        when (arguments?.getString(BUNDLE_TITLE)) {
            getString(R.string.category) -> {
                prefSession.setCategory(prefName)
            }
            getString(R.string.language) -> {
                prefSession.setLanguage(prefName)
            }

            getString(R.string.country) -> {
                prefSession.setCountry(prefName)
            }
        }
    }

    private fun initIntent() {
        tvPrefName.text = arguments?.getString(BUNDLE_TITLE)
        when (arguments?.getString(BUNDLE_TITLE)) {

            getString(R.string.category) -> {
                resources.getStringArray(R.array.categories).forEach { item ->
                    arrayList.add(Preference(item, item))
                }
            }
            getString(R.string.country) -> {
                (0 until resources.getStringArray(R.array.country).size).forEach { i ->
                    arrayList.add(Preference(resources.getStringArray(R.array.country)[i],
                        resources.getStringArray(R.array.country_short)[i]))
                }
            }
            getString(R.string.sorted_by) -> { // not implemented yet
                resources.getStringArray(R.array.categories).forEach { item ->
                    arrayList.add(Preference(item, ""))
                }
            }
            getString(R.string.language) -> {
                (0 until resources.getStringArray(R.array.language).size).forEach { i ->
                    arrayList.add(Preference(resources.getStringArray(R.array.language)[i],
                        resources.getStringArray(R.array.language_short)[i]))
                }
            }
        }
        prefAdapter.differ.submitList(arrayList)
    }
}