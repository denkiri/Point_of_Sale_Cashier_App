package com.denkiri.poscashier.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.cashier.Users
import com.denkiri.poscashier.utils.OnCustomerItemClick

class CashierAdapter(private val type: Int, private  val context: Context, private  var modelList: List<Users>?, private val recyclerListener: OnCustomerItemClick): RecyclerView.Adapter<CashierViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashierViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.cashier_item_b,parent,false)
        return CashierViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CashierViewHolder, position: Int) {
        val model= modelList!![position]
        holder.name.text =model.cashierName
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Users>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}