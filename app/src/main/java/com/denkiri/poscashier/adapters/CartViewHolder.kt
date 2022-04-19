package com.denkiri.poscashier.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnCartItemClick
import java.lang.ref.WeakReference
class CartViewHolder (type: Int, itemView: View, listener: OnCartItemClick): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private val listenerWeakReference: WeakReference<OnCartItemClick> = WeakReference(listener)
    var itemVew: View
    var code: TextView
    var quantityValue:TextView
    var remove:ImageView
    var add :ImageView
    var delete:ImageView
    var productName: TextView
    var priceValue: TextView
    var discountValue:TextView
    var unit: TextView
    var cardView: CardView
    var totalAmountValue: TextView
    var vatValue: TextView
    var amountValue:TextView
    override fun onClick(v: View?) {

        if ( v == add) {
            listenerWeakReference.get()?.increase(adapterPosition)
        }
        if ( v == remove) {
            listenerWeakReference.get()?.decrease(adapterPosition)
        }
        if ( v == delete) {
            listenerWeakReference.get()?.delete(adapterPosition)
        }
        if ( v == cardView) {
           // listenerWeakReference.get()?.onClickListener(adapterPosition)
        }

    }
    init {
        this.itemVew=itemView
        code=itemView.findViewById(R.id.code)
        quantityValue=itemView.findViewById(R.id.quantityValue)
        productName=itemView.findViewById(R.id.productName)
        remove=itemView.findViewById(R.id.remove)
        add=itemView.findViewById(R.id.add)
        delete=itemView.findViewById(R.id.delete)
        cardView=itemView.findViewById(R.id.card_view)
        priceValue=itemView.findViewById(R.id.priceValue)
        discountValue=itemView.findViewById(R.id.discountValue)
        unit=itemView.findViewById(R.id.unit)
        totalAmountValue=itemView.findViewById(R.id.totalAmountValue)
        vatValue=itemView.findViewById(R.id.vatValue)
        amountValue=itemView.findViewById(R.id.amountValue)
        if (type==0) {
            add.setOnClickListener(this)
            remove.setOnClickListener(this)
            delete.setOnClickListener(this)
            cardView.setOnClickListener(this)
        }

    }


}