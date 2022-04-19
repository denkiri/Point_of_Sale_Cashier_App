package com.denkiri.poscashier.adapters
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnExpenseClick
import java.lang.ref.WeakReference
class ExpenseItemViewHolder (type: Int, itemView: View, listener: OnExpenseClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v ==  card_view  ) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnExpenseClick> = WeakReference(listener)
    var itemVew: View
    var card_view: CardView
    var expenseName: TextView
    init {
        this.itemVew=itemView
        card_view=itemView.findViewById(R.id.card_view)
        expenseName=itemView.findViewById(R.id.expenseName)
        if (type==0) {
            card_view.setOnClickListener(this)
        }
    }
}