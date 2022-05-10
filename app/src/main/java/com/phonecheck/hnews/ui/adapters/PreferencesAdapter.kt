package com.phonecheck.hnews.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.phonecheck.hnews.databinding.ItemPreferenceBinding
import com.phonecheck.hnews.ui.models.Preference
import kotlinx.android.synthetic.main.item_preference.view.*

class PreferencesAdapter(private val onItemClick: (preference: Preference) -> Unit) :
    RecyclerView.Adapter<PreferencesAdapter.ViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Preference>() {
        override fun areItemsTheSame(oldItem: Preference, newItem: Preference): Boolean {
            return oldItem.prefName == newItem.prefName
        }

        override fun areContentsTheSame(oldItem: Preference, newItem: Preference): Boolean {
           return true
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemPreferenceBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(binding: ItemPreferenceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(preference: Preference) {

            itemView.apply {
                tvPrefName.text = preference.prefName
                tvPrefDetails.text = preference.details
                setOnClickListener {
                    onItemClick(preference)
                }
            }
        }
    }
}

