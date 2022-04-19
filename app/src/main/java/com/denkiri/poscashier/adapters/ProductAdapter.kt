package com.denkiri.poscashier.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.home.AddToCartFragment
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.utils.OnProductClick
import java.text.NumberFormat
import java.util.*


class ProductAdapter(
    private val type: Int,
    private val context: Context,
    private var modelList: ArrayList<Product>?,
    private val recyclerListener: OnProductClick
): RecyclerView.Adapter<ProductViewHolder>()  {
    var filterList = ArrayList<Product>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(type, itemView!!, recyclerListener)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        if (model.reorder!=null && model.qtyLeft!=null) {
            if (model.reorder!! >= model.qtyLeft!!) {
                holder.quantity_left.setTextColor(Color.parseColor("#ffcc0000"))
                holder.quantity.setTextColor(Color.parseColor("#ffcc0000"))
            } else {
                holder.quantity_left.setTextColor(Color.parseColor("#4CAF50"))
                holder.quantity.setTextColor(Color.parseColor("#4CAF50"))
            }
        }
        holder.productName.text = model.productName
        holder.quantity_left.text= model.qtyLeft.toString()
        holder.price.text=format.format(model.price?.toDouble()).toString()
        holder.unit.text=model.unit
      //  holder.code.text=model.productCode
        holder.productDescription.text=model.descriptionName
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<Product>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) filterList = modelList!! else {
                    var filteredList = ArrayList<Product>()
                    modelList
                        ?.filter { it.productName!!.toLowerCase(Locale.ROOT).contains(
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
                filterList = results?.values as ArrayList<Product>
                notifyDataSetChanged()
            }

        }
    }
    fun filterList(filteredProductList: ArrayList<Product>) {

        this.modelList = filteredProductList
        notifyDataSetChanged()
    }

}