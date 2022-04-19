package com.denkiri.poscashier.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnCategoryItemClick
import com.denkiri.poscashier.utils.OnProductClick
import java.lang.ref.WeakReference

class ProductViewHolder (type: Int, itemView: View, listener: OnProductClick): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private val listenerWeakReference: WeakReference<OnProductClick> = WeakReference(listener)
    var itemVew:View
    var quantity_left: TextView
    var productName: TextView
    var price: TextView
    var unit: TextView
    var cardView:CardView
  //  var code: TextView
    var quantity:TextView
    var productDescription:TextView
    lateinit var radio: RadioButton
    override fun onClick(v: View?) {

        if ( v == radio) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }

        if ( v == cardView) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }



    }
    init {
        this.itemVew=itemView
        quantity_left=itemView.findViewById(R.id.quantityLeft)
        productName=itemView.findViewById(R.id.productName)
        productDescription=itemView.findViewById(R.id.productDescription)
        price=itemView.findViewById(R.id.priceValue)
        unit=itemView.findViewById(R.id.unit)
        cardView=itemView.findViewById(R.id.card_view)
      //  code=itemView.findViewById(R.id.code)
        quantity=itemView.findViewById(R.id.quantity)
        if (type==0) {
            radio = itemView.findViewById(R.id.radioUse)
            cardView=itemView.findViewById(R.id.card_view)
            radio.setOnClickListener(this)
            cardView.setOnClickListener(this)
        }

    }


}