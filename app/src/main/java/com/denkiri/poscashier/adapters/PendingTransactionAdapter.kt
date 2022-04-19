package com.denkiri.poscashier.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.customer.Customer
import com.denkiri.poscashier.models.pending.PendingTransaction
import com.denkiri.poscashier.utils.OnCustomerClick
import com.denkiri.poscashier.utils.OnProductClick
import java.util.ArrayList

class PendingTransactionAdapter  (private val type: Int, private  val context: Context, private  var modelList: List<PendingTransaction>?, private val recyclerListener: OnProductClick):
    RecyclerView.Adapter<PendingTransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingTransactionViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.pending_payment_item,parent,false)
        return PendingTransactionViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: PendingTransactionViewHolder, position: Int) {
        val model= modelList!![position]
        holder.invoice.text =model.invoiceNumber
        holder.dateValue.text=model.date
        holder.paymentMode.text=model.type
        holder.paymentDetails.text=model.paymentDetails
        holder.cashierName.text=model.cashier
        holder.mobileValue.text=model.phoneNumber
        holder.amountValue.text=model.amount

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<PendingTransaction>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredTransactionList: ArrayList<PendingTransaction>) {
        this.modelList = filteredTransactionList
        notifyDataSetChanged()
    }
}