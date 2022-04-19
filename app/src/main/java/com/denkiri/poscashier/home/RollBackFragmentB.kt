package com.denkiri.poscashier.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.poscashier.R
import com.denkiri.poscashier.ReceiptActivityB
import com.denkiri.poscashier.RollBackActivity
import com.denkiri.poscashier.adapters.RollBackTransactionAdapter
import com.denkiri.poscashier.models.order.RollBackItems
import com.denkiri.poscashier.models.order.RollBackItemsData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.utils.OnProductClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_roll_back.*
import kotlinx.android.synthetic.main.fragment_roll_back_b.*
import kotlinx.android.synthetic.main.fragment_roll_back_b.avi
import kotlinx.android.synthetic.main.fragment_roll_back_b.empty_button
import kotlinx.android.synthetic.main.fragment_roll_back_b.empty_layout
import kotlinx.android.synthetic.main.fragment_roll_back_b.empty_layoutB
import kotlinx.android.synthetic.main.fragment_roll_back_b.empty_text
import kotlinx.android.synthetic.main.fragment_roll_back_b.itemsswipetorefresh
import kotlinx.android.synthetic.main.fragment_roll_back_b.main
import kotlinx.android.synthetic.main.fragment_roll_back_b.recyclerView
import kotlinx.android.synthetic.main.fragment_roll_back_b.searchView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RollBackFragmentB : Fragment() {
    var invoice:String?=null
    var paymentType:String?=null
    lateinit var pendingOrdersAdapter: RollBackTransactionAdapter
    private lateinit var pendingOrders: ArrayList<RollBackItems>
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        invoice=(activity as RollBackActivity).invoice
        paymentType=(activity as RollBackActivity).paymentType
        init()
        initView()
        observers()
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            //  initView()
            //  observers()
            itemsswipetorefresh.isRefreshing = false
        }
    }
    private fun setUpInvoices(products: ArrayList<RollBackItems>) {
        this.pendingOrders = products
        pendingOrdersAdapter.refresh(this.pendingOrders)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun setStatus(data: Resource<RollBackItemsData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
              //  empty_text.text = data.message
              //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }


            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener { init() }
        }
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getItems(invoice)
        }
    }
    fun observers(){
        viewModel.observeItemsData().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {
                    setUpInvoices(it.data.order as ArrayList<RollBackItems>)
                } else {
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                    setUpInvoices(ArrayList())
                }
            }

        })
        /*
        viewModel.observeCartAction().observe(viewLifecycleOwner, {
           // setStatusB(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let { it1 -> viewModel.saveProducts(it1) }
                it.data?.let { it1 -> viewModel.saveOrders(it1) }
                //   it.data?.let { it1 -> viewModel.saveTotalAmount(it1) }
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {
                    init()
                } else {
                    init()
                }
            }

        })

         */
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roll_back_b, container, false)
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
    private fun initView(){
        pendingOrders= ArrayList()
        pendingOrdersAdapter = context?.let {
            RollBackTransactionAdapter(0, it,pendingOrders, object : OnProductClick {
                override fun selected(pos: Int) {
                    viewModel.deleteInvoiceItems(pendingOrders[pos].transactionId.toString(),invoice.toString(),pendingOrders[pos].qty.toString(),pendingOrders[pos].product.toString(),pendingOrders[pos].amount.toString(),paymentType.toString())

                }

                override fun onClickListener(position1: Int) {

                }




            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = pendingOrdersAdapter
        pendingOrdersAdapter.notifyDataSetChanged()
    }
    fun filter(text: String) {
        val filteredProductAry: ArrayList<RollBackItems> = ArrayList()
        val productAry : ArrayList<RollBackItems> = pendingOrders
        for (eachProduct in productAry) {
            if (eachProduct.name!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredProductAry.add(eachProduct)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        pendingOrdersAdapter.filterList(filteredProductAry)
        pendingOrders=filteredProductAry

        pendingOrdersAdapter.notifyDataSetChanged()
        if (pendingOrdersAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RollBackFragmentB.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RollBackFragmentB().apply {
                arguments = Bundle().apply {

                }
            }
    }
}