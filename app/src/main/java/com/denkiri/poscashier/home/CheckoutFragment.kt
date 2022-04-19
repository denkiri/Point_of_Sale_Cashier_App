package com.denkiri.poscashier.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.poscashier.R
import com.denkiri.poscashier.adapters.CustomerAdapter
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.customer.Customer
import com.denkiri.poscashier.models.customer.CustomerData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.receipt.ReceiptActivity
import com.denkiri.poscashier.storage.PreferenceManager
import com.denkiri.poscashier.utils.DatePickerHelper
import com.denkiri.poscashier.utils.OnCustomerClick
import com.denkiri.poscashier.utils.Validator
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_checkout.*
import java.util.*
import kotlin.collections.ArrayList
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : RoundedBottomSheetDialogFragment(){

    private lateinit var viewModel: CartViewModel
    lateinit var customerAdapter: CustomerAdapter
    private lateinit var customers: ArrayList<Customer>
    lateinit var datePicker: DatePickerHelper
    var paymentType:String?=null
    var customer:String?=null
    var cashier:String?=null
    var businessCode:String?=null
    var amount:String?="0"
    var dueDates:String?=null
    var id:String?=null
 // lateinit var  bottomSheet:RoundedBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowManager.LayoutParams.MATCH_PARENT
        arguments?.let {
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //SEARCH FILTER
                filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })

    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getCustomer()
        }}
    fun saveCashSale() {

        viewModel.saveCashSale(PreferenceManager(requireContext()).getInvoiceNumber(), cashier.toString(), paymentType.toString(), amount.toString(), dueDates.toString(), amount.toString(), customer.toString())
    }
        fun saveCreditSale(){
            if (validate()) {
                viewModel.saveCreditSale(PreferenceManager(requireContext()).getInvoiceNumber(), cashier.toString(), paymentType.toString(), amount.toString(), tvDate.text.toString(), amount.toString(), customer.toString())
            }
        }

    fun saveMpesaSale(){
        if (validateB()) {
            viewModel.saveMpesaSale(PreferenceManager(requireContext()).getInvoiceNumber(), cashier.toString(), paymentType.toString(), amount.toString(), customer.toString(),mobile.text.toString())
        }
    }




    private fun validate(): Boolean {
        if (customer==null){
            Toasty.error(requireContext(),"Select Customer", Toast.LENGTH_LONG, false).show()
            return false
        }
        if (tvDate.text.toString()=="00-00-0000"){
            Toasty.error(requireContext(),"Set Due Date", Toast.LENGTH_LONG, false).show()
            return false
        }


        return true
    }
    private fun validateB(): Boolean {
        if (!Validator.isValidPhone(mobile)) {
            return false
        }



        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        val bundle = arguments
        paymentType=bundle?.getString("paymentType").toString()
        amount= bundle?.getString("amount").toString()
        id=PreferenceManager(requireContext()).getInvoiceNumber()
        paymentModeText.text=bundle?.getString("paymentType").toString()
     if(paymentType=="cash"){
         mobile.visibility=View.INVISIBLE
         dueDate.visibility=View.INVISIBLE
         layoutC.visibility=View.INVISIBLE
     }
        if(paymentType=="mpesa"){
            dueDate.visibility=View.INVISIBLE
            layoutC.visibility=View.INVISIBLE
        }
        if(paymentType=="credit"){
            mobile.visibility=View.INVISIBLE
        }
        cancel.setOnClickListener {
            dismiss()
        }
        initView()
        init()
        setUpUi()
        observers()
        save.setOnClickListener {
            checkSubscription()


        }
        datePicker = DatePickerHelper(requireContext(),true)
        btSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
    }



private fun saveSale(){
    if(paymentType=="cash"){
        saveCashSale()

    }
    if(paymentType=="credit"){
        saveCreditSale()

    }
    if(paymentType=="mpesa"){
    //    Toasty.error(requireContext(),amount.toString(), Toast.LENGTH_LONG,false).show()
     saveMpesaSale()

    }
}
    private fun checkSubscription(){
        viewModel.checkSubscription(businessCode.toString())
    }
    private fun setStatus(data: Resource<TransactionData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            cancel.visibility=View.INVISIBLE
            save.isEnabled = false
            save.isClickable = false
            save.setBackgroundColor(resources.getColor(R.color.divider))
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            save.isEnabled = true
            save.isClickable = true
            save.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                cancel.visibility=View.VISIBLE
                 empty_text.text = data.message
                 view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }

            }

         //   empty_layout.visibility = View.VISIBLE
         //   main.visibility = View.GONE
        //    empty_button.setOnClickListener {
           //     saveSale()
           // }
        }
    }
    private fun setStatusC(data: Resource<PaymentResponseData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            cancel.visibility=View.INVISIBLE
            save.isEnabled = false
            save.isClickable = false
            save.setBackgroundColor(resources.getColor(R.color.divider))
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            cancel.visibility=View.VISIBLE
            avi.visibility = View.GONE
            save.isEnabled = true
            save.isClickable = true
            save.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                cancel.visibility=View.VISIBLE
                empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }

            }

            //   empty_layout.visibility = View.VISIBLE
            //   main.visibility = View.GONE
            //    empty_button.setOnClickListener {
            //     saveSale()
            // }
        }
    }
    private fun setStatusB(data: Resource<CustomerData>) {
        empty_layoutB.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            cancel.visibility=View.INVISIBLE
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            cancel.visibility=View.VISIBLE
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }


        if (status == Status.ERROR) {
            if (data.message != null) {
                cancel.visibility=View.VISIBLE
                // empty_text.text = data.message
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layoutB.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            empty_iconB.setOnClickListener { init() }
        }
    }
    private fun setUpCustomers(customers: ArrayList<Customer>) {
        this.customers = customers
        customerAdapter.refresh(this.customers)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun initView(){
        customers = ArrayList()
        customerAdapter = context?.let {
            CustomerAdapter(0, it, customers, object : OnCustomerClick {


                override fun selected(pos: Int) {
                    customers[pos].customerName?.let { it1 -> Toasty.success(requireContext(), it1, Toast.LENGTH_SHORT, true).show() }
           customer= customers[pos].customerName
                    customerSelected.text=customers[pos].customerName


                }

                override fun onClickListener(position1: Int) {

                }

            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = customerAdapter
        customerAdapter.notifyDataSetChanged()
    }
    fun observers() {
        viewModel.observeCustomer().observe(
                viewLifecycleOwner,
                {
                    setStatusB(it)
                    if (it.status == Status.SUCCESS) {

                        if (it.data?.customers != null && !it.data.customers!!.isEmpty()) {
                            setUpCustomers(it.data.customers as ArrayList<Customer>)
                        } else {
                            setUpCustomers(ArrayList())
                        }
                    }


                })
        viewModel.observeSubscriptionAction().observe( viewLifecycleOwner,
                {
                    setStatusC(it)
                    if (it.status == Status.SUCCESS) {

                        if (it.data?.status ==1 && it.data.error==false) {
                          //  it.data?.message?.let { it1 -> Toasty.success(requireContext(), it1, Toast.LENGTH_LONG,false).show() }
                            saveSale()
                        } else {
                          //  Toasty.error(requireContext(),"Subscribe", Toast.LENGTH_LONG,false).show()

                        }
                    }


                }

        )
        viewModel.observeTransactionData().observe(
                viewLifecycleOwner,
                {
                   setStatus(it)
                    if (it.status == Status.SUCCESS) {
                        viewModel.saveInvoiceNumber("")
                        dismiss()
                      //  requireActivity().supportFragmentManager.beginTransaction()
                               // .replace(R.id.container, HomeFragment())
                              //  .commitNow()
                       // val intent = Intent(context, MainActivity::class.java)
                      //  startActivity(intent)
                        val intent = Intent(activity, ReceiptActivity::class.java)
                        intent.putExtra("id",id)
                        intent.putExtra("paymentMode",paymentType)
                        startActivity(intent)

                    }
                    if (it.status == Status.ERROR) {
                        Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()
                    }
                })

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                CheckoutFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    fun filter(text: String) {
        val filteredCustomerAry: ArrayList<Customer> = ArrayList()
        val customerAry : ArrayList<Customer> = customers
        for (eachProduct in customerAry) {
            if (eachProduct.customerName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredCustomerAry.add(eachProduct)
            }

        }
        //calling a method of the adapter class and passing the filtered list
        customerAdapter.filterList(filteredCustomerAry)
        customers=filteredCustomerAry

        customerAdapter.notifyDataSetChanged()
        if (customerAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    fun setUpUi() {
        viewModel.getOuthProfile().observe(viewLifecycleOwner, {
            try {
                cashier=it.cashierName
                businessCode=it.businessCode


            } catch (e: Exception) {
            }
        })
    }
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        val minDate = Calendar.getInstance()
        minDate.set(Calendar.HOUR_OF_DAY, 0)
        minDate.set(Calendar.MINUTE, 0)
        minDate.set(Calendar.SECOND, 0)
        datePicker.setMinDate(minDate.timeInMillis)
        datePicker.showDialog(y,m,d, object : DatePickerHelper.Callback {
            override fun onDateSelected(year: Int,month: Int,dayofMonth: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                tvDate.text = "${dayStr}-${monthStr}-${year}"
            }
        })
    }

}