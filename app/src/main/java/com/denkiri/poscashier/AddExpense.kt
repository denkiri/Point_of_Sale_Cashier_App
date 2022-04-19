package com.denkiri.poscashier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.poscashier.home.HomeViewModel
import com.denkiri.poscashier.home.RecordIncomeFragment
import com.denkiri.poscashier.models.expense.ExpenseData
import com.denkiri.poscashier.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_expense.*




class AddExpense : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false)
    }
    private fun setStatus(data: Resource<ExpenseData>) {
        val status: Status = data.status
        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        if (status == Status.ERROR) {
            if (data.message != null) {
                //view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
                Toasty.error(requireContext(), data.message.toString(), Toast.LENGTH_LONG, true).show()
            }
        }
    }
    fun observers(){
        viewModel.observeExpenseAction().observe(viewLifecycleOwner, {
            setStatus(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.expenses != null && it.data.expenses!!.isNotEmpty()) {
                    Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //  val navController = Navigation.findNavController(requireView())
                    // navController.navigate(R.id.nav_category)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, RecordIncomeFragment())
                        .commitNow()

                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG, true).show()

                }

            }

        })

    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(expense_title)) {
            return false
        }
        if (!Validator.isValidPrice(expense_amount)) {
            return false
        }
        return true
    }
    fun  addExpense(){
        if (validate()) {
            viewModel.addExpense(expense_title.text.toString(),expense_amount.text.toString())
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddExpense.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AddExpense().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        addExpense.setOnClickListener { addExpense() }
        observers()

    }
}