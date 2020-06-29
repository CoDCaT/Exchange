package com.test.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.api.model.Rates
import kotlinx.android.synthetic.main.item_rate.view.*

class RateListItemVH constructor(
    val parent: ViewGroup,
    private val rateListListener: IRateListListener
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
    ) {

    fun bind(item: Rates) {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() == ".") {
                    itemView.editRateForExchange.apply {
                        setText("")
                        append("0.")
                    }
                } else {
                    if (itemView.editRateForExchange.hasFocus()) {
                        rateListListener.onExchangeValueChanged(if (s.toString() == "") 0.0 else s.toString().toDouble())
                    }
                }
            }
        }
        itemView.textExchangeValue.text = item.name
        itemView.textCurrentTotal.text =
            String.format(parent.context.getString(R.string.you_have), String.format("%.2f", item.totalValue).replace(",", "."))
        itemView.textCurrentRate.text = String.format(parent.context.getString(R.string.rateFormat), item.name, item.exchangeValue, item.exchangeName)

        itemView.editRateForExchange.removeTextChangedListener(listener)
        itemView.editRateForExchange.addTextChangedListener(listener)
        if (item.forExchangeValue.toString() != "0.0") {
            itemView.editRateForExchange.setText(String.format("%.2f", item.forExchangeValue).replace(",", "."))
        } else {
            itemView.editRateForExchange.setText("")
        }
//        itemView.editRateForExchange.onEdit {
//            if (it == ".") {
//                itemView.editRateForExchange.apply {
//                    setText("")
//                    append("0.")
//                }
//            } else {
//                if (itemView.editRateForExchange.isFocused) {
//                    rateListListener.onExchangeValueChanged(if (it == "") 0.0 else it.toDouble())
//                }
//            }
//        }
    }
}