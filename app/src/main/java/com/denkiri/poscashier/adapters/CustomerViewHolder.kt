package com.denkiri.poscashier.adapters
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnCustomerClick
import java.lang.ref.WeakReference
class CustomerViewHolder (type: Int, itemView: View, listener: OnCustomerClick): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
    override fun onClick(v: View?) {
        if (v == cardView) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }

    }
    private val listenerWeakReference: WeakReference<OnCustomerClick> = WeakReference(listener)
    var itemVew: View
    var cardView: CardView
    var firstName: TextView
    var middleName: TextView
    var mobile:TextView
    var lastName: TextView
    var code:TextView
    init {
        this.itemVew = itemView
        cardView=itemView.findViewById(R.id.card_view)
        firstName = itemView.findViewById(R.id.firstName)
        middleName = itemView.findViewById(R.id.middleName)
        lastName = itemView.findViewById(R.id.lastName)
        mobile=itemView.findViewById(R.id.mobile)
        code=itemView.findViewById(R.id.code)

        if (type == 0) {
            cardView.setOnClickListener(this)
        }
    }
}