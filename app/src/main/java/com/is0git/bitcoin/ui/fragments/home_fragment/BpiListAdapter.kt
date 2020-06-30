package com.is0git.bitcoin.ui.fragments.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.is0git.bitcoin.R
import com.is0git.bitcoin.data.entities.RoomBpiData
import com.is0git.bitcoin.databinding.BitcoinListItemBinding
import com.is0git.bitcoin.utils.setImageDrawable
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BpiListAdapter @Inject constructor() :
    ListAdapter<RoomBpiData, BpiListAdapter.BpiListViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BpiListViewHolder {
        val binding =
            BitcoinListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val colorIdRes = when (viewType) {
            0 -> R.color.colorPrimary
            1 -> R.color.colorSecondary
            2 -> R.color.colorSecondaryVariant
            else -> R.color.colorPrimary
        }
        binding.point.setImageResource(colorIdRes)
        return BpiListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BpiListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            currencyText.text = item.code
            currencyDescription.text = item.description
            ratioText.text = item.rate
            currencyImage.setImageDrawable(item.code)
        }
    }

    class BpiListViewHolder(val binding: BitcoinListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return position % 3
    }
}

val diffCallback = object : DiffUtil.ItemCallback<RoomBpiData>() {
    override fun areItemsTheSame(oldItem: RoomBpiData, newItem: RoomBpiData): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: RoomBpiData, newItem: RoomBpiData): Boolean {
        return oldItem == newItem
    }

}