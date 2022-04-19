package com.denkiri.poscashier.adapters
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnProductClick
import java.lang.ref.WeakReference
class PendingTransactionViewHolder (type: Int, itemView: View, listener: OnProductClick): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private val listenerWeakReference: WeakReference<OnProductClick> = WeakReference(listener)
    var itemVew: View
    var invoice: TextView
    var paymentMode: TextView
    var dateValue: TextView
    var amountValue: TextView
    var paymentDetails: TextView
    var cashierName: TextView
    var mobileValue: TextView
    var receipt:Button
    var payButton:Button
    override fun onClick(v: View?) {

        if ( v == receipt) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }

        if ( v == payButton) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }

    }
    init {
        this.itemVew=itemView
        invoice=itemView.findViewById(R.id.invoice)
        paymentMode=itemView.findViewById(R.id.paymentMode)
        dateValue=itemView.findViewById(R.id.dateValue)
        amountValue=itemView.findViewById(R.id.amountValue)
        paymentDetails=itemView.findViewById(R.id.paymentDetails)
        cashierName=itemView.findViewById(R.id.cashierName)
        mobileValue=itemView.findViewById(R.id.mobileValue)
        receipt = itemView.findViewById(R.id.receipt)
        payButton=itemView.findViewById(R.id.payButton)
        if (type==0) {
            receipt = itemView.findViewById(R.id.receipt)
            payButton=itemView.findViewById(R.id.payButton)
            receipt.setOnClickListener(this)
            payButton.setOnClickListener(this)

        }

    }


}