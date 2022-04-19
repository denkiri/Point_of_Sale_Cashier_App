package com.denkiri.poscashier.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.RollBackItems
import com.denkiri.poscashier.utils.OnProductClick
import java.text.NumberFormat
import java.util.*
class RollBackTransactionAdapter  (
    private val type: Int,
    private val context: Context,
    private var modelList: java.util.ArrayList<RollBackItems>?,
    private val recyclerListener: OnProductClick
): RecyclerView.Adapter<RollBackTransactionViewHolder>()  {
    var filterList = java.util.ArrayList<RollBackItems>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollBackTransactionViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.roll_back_product_item, parent, false)
        return RollBackTransactionViewHolder(type, itemView!!, recyclerListener)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RollBackTransactionViewHolder, position: Int) {
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
    fun refresh(modelList: java.util.ArrayList<RollBackItems>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) filterList = modelList!! else {
                    var filteredList = java.util.ArrayList<RollBackItems>()
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
                filterList = results?.values as java.util.ArrayList<RollBackItems>
                notifyDataSetChanged()
            }

        }
    }
    fun filterList(filteredProductList: java.util.ArrayList<RollBackItems>) {

        this.modelList = filteredProductList
        notifyDataSetChanged()
    }

}