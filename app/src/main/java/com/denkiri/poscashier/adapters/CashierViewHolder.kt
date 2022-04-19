package com.denkiri.poscashier.adapters

import android.view.View

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnCustomerItemClick
import java.lang.ref.WeakReference

class CashierViewHolder (type: Int, itemView: View, listener: OnCustomerItemClick): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    override fun onClick(p0: View?) {
        if ( p0== cardView  ) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }

    }
    private val listenerWeakReference: WeakReference<OnCustomerItemClick> = WeakReference(listener)
    var itemVew: View
    var cardView:CardView
    var name: TextView
    init {
        this.itemVew=itemView
        name=itemView.findViewById(R.id.name)
        cardView=itemView.findViewById(R.id.card_view)
        if (type==0) {
            cardView = itemView.findViewById(R.id.card_view)
            cardView.setOnClickListener(this)
        }
    }
}