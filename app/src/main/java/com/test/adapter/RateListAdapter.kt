package com.test.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.test.api.model.Rates
import com.test.di.ActivityScope
import javax.inject.Inject

@ActivityScope
class RateListAdapter @Inject constructor() :
    RecyclerView.Adapter<RateListItemVH>() {

    private val differ = AsyncListDiffer(this, RateListDiffUtil()
    )
    private lateinit var rateListListener: IRateListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateListItemVH {
        return RateListItemVH(parent, rateListListener)
    }

    override fun onBindViewHolder(holder: RateListItemVH, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setRateList(chatListIds: List<Rates>) {
        differ.submitList(chatListIds)
    }

    fun setListener(rateListListener: IRateListListener) {
        this.rateListListener = rateListListener
    }
}
