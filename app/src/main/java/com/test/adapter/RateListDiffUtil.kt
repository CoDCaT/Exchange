package com.test.adapter

import androidx.recyclerview.widget.DiffUtil
import com.test.api.model.Rates

class RateListDiffUtil : DiffUtil.ItemCallback<Rates>() {
    override fun areItemsTheSame(oldItem: Rates, newItem: Rates): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.totalValue == newItem.totalValue &&
                oldItem.value == newItem.value &&
                oldItem.exchangeName == newItem.exchangeName &&
                oldItem.exchangeValue == newItem.exchangeValue &&
                oldItem.forExchangeValue == newItem.forExchangeValue
    }

    override fun areContentsTheSame(oldItem: Rates, newItem: Rates): Boolean {
        return oldItem == newItem
    }
}
