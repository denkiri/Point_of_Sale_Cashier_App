package com.denkiri.poscashier.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.PendingOrders
import com.denkiri.poscashier.utils.OnCartItemClick
import com.denkiri.poscashier.utils.OnProductClick
import java.text.NumberFormat
import java.util.*

class RollBackProductAdapter (
    private val type: Int,
    private val context: Context,
    private var modelList: java.util.ArrayList<PendingOrders>?,
    private val recyclerListener: OnProductClick
): RecyclerView.Adapter<RollBackProductViewHolder>()  {
    var filterList = java.util.ArrayList<PendingOrders>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollBackProductViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.roll_back_product_item, parent, false)
        return RollBackProductViewHolder(type, itemView!!, recyclerListener)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RollBackProductViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.productName.text = model.name
        holder.code.text=model.product
        holder.priceValue.text=format.format(model.price).toString()
        holder.vatValue.text=format.format(model.vat).toString()
        holder.discountValue.text=format.format(model.discount).toString()
        holder.amountValue.text=format.format(model.amount).toString()
        holder.totalAmountValue.text=format.format(model.totalAmount).toString()
        holder.quantityValue.text=model.qty
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: java.util.ArrayList<PendingOrders>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) filterList = modelList!! else {
                    var filteredList = java.util.ArrayList<PendingOrders>()
                    modelList
                        ?.filter { it.name!!.toLowerCase(Locale.ROOT).contains(
                            charString.toLowerCase(
                                Locale.ROOT
                            )
                        )
                        }
                        ?.forEach { filteredList.add(it) }
                    filteredList = modelList!!
                }
                return FilterResults().apply { values = filterList }
            }


            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as java.util.ArrayList<PendingOrders>
                notifyDataSetChanged()
            }

        }
    }
    fun filterList(filteredProductList: java.util.ArrayList<PendingOrders>) {

        this.modelList = filteredProductList
        notifyDataSetChanged()
    }

}