package com.denkiri.poscashier.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.expense.Expenses
import com.denkiri.poscashier.utils.OnExpenseClick
import java.util.ArrayList
class ExpenseItemAdapter(private val type: Int, private  val context: Context, private  var modelList: List<Expenses>?, private val recyclerListener: OnExpenseClick): RecyclerView.Adapter<ExpenseItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.select_expense_item,parent,false)
        return ExpenseItemViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {
        val model= modelList!![position]
        holder.expenseName.text =model.name

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Expenses>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredExpenseList: ArrayList<Expenses>) {
        this.modelList = filteredExpenseList
        notifyDataSetChanged()
    }

}