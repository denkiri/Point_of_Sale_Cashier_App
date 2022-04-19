package com.denkiri.poscashier.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.report.SalesReport
import com.denkiri.poscashier.utils.OnReportClick
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
class SalesAdapter(private val type: Int, private val context: Context, private var modelList: ArrayList<SalesReport>, private val recyclerListener: OnReportClick): RecyclerView.Adapter<SalesViewHolder>() {
    var filterList = ArrayList<SalesReport>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.sales_item,parent,false)
        return SalesViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.paymentMode.text =model.type
        holder.invoiceNumber.text=model.invoiceNumber
        holder.date.text=model.date
        holder.customerName.text=model.name
        holder.sales.text=format.format(model.amount).toString()
        holder.balance.text=format.format(model.balance).toString()

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<SalesReport>){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredSalesList: ArrayList<SalesReport>) {

        this.modelList = filteredSalesList
        notifyDataSetChanged()
    }
}