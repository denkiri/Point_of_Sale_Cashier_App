package com.denkiri.poscashier.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnProductClick
import java.lang.ref.WeakReference

class RollBackTransactionViewHolder (type: Int, itemView: View, listener: OnProductClick): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private val listenerWeakReference: WeakReference<OnProductClick> = WeakReference(listener)
    var itemVew: View
    var code: TextView
    var quantityValue: TextView
    var delete: Button
    var productName: TextView
    var priceValue: TextView
    var discountValue: TextView
    var unit: TextView
    var cardView: CardView
    var totalAmountValue: TextView
    var vatValue: TextView
    var amountValue: TextView
    override fun onClick(v: View?) {
        if ( v == delete) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }
        if ( v == cardView) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }

    }
    init {
        this.itemVew=itemView
        code=itemView.findViewById(R.id.code)
        quantityValue=itemView.findViewById(R.id.quantityValue)
        productName=itemView.findViewById(R.id.productName)
        delete=itemView.findViewById(R.id.delete)
        cardView=itemView.findViewById(R.id.card_view)
        priceValue=itemView.findViewById(R.id.priceValue)
        discountValue=itemView.findViewById(R.id.discountValue)
        unit=itemView.findViewById(R.id.unit)
        totalAmountValue=itemView.findViewById(R.id.totalAmountValue)
        vatValue=itemView.findViewById(R.id.vatValue)
        amountValue=itemView.findViewById(R.id.amountValue)
        if (type==0) {
            delete.setOnClickListener(this)
            cardView.setOnClickListener(this)
        }

    }


}