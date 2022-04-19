package com.denkiri.poscashier.adapters
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.utils.OnReportClick
import java.lang.ref.WeakReference

class CashViewHolder (type: Int, itemView: View, listener: OnReportClick): RecyclerView.ViewHolder(itemView), View.OnClickListener  {
    private val listenerWeakReference: WeakReference<OnReportClick> = WeakReference(listener)
    var itemVew: View
    var paymentMode: TextView
    var invoiceNumber: TextView
    var date: TextView
    var customerName: TextView
    var sales: TextView
    var receipt: Button
    init {
        this.itemVew=itemView
        paymentMode=itemView.findViewById(R.id.paymentMode)
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)
        date=itemView.findViewById(R.id.date)
        customerName=itemView.findViewById(R.id.customerName)
        sales=itemView.findViewById(R.id.sales)
        receipt=itemView.findViewById(R.id.receipt)

        if (type==0) {
            receipt = itemView.findViewById(R.id.receipt)
            receipt.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        listenerWeakReference.get()?.selected(adapterPosition)
    }

}