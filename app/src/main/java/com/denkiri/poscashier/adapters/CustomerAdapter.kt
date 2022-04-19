package com.denkiri.poscashier.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.customer.Customer
import com.denkiri.poscashier.utils.OnCustomerClick
import java.util.ArrayList

class CustomerAdapter (private val type: Int, private  val context: Context, private  var modelList: List<Customer>?, private val recyclerListener: OnCustomerClick):
        RecyclerView.Adapter<CustomerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder{
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.customer_item,parent,false)
        return CustomerViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val model= modelList!![position]
        holder.firstName.text =model.firstName
        holder.middleName.text=model.middleName
        holder.lastName.text=model.lastName
        holder.code.text=model.membershipNumber
        holder.mobile.text=model.contact

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Customer>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredCustomerList: ArrayList<Customer>) {
        this.modelList = filteredCustomerList
        notifyDataSetChanged()
    }
}