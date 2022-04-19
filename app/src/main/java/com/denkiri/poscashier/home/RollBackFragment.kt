package com.denkiri.poscashier.home

import android.content.Intent
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
import com.denkiri.poscashier.adapters.PendingTransactionAdapter
import com.denkiri.poscashier.adapters.RollBackProductAdapter
import com.denkiri.poscashier.models.order.Order
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.order.PendingOrders
import com.denkiri.poscashier.models.order.PendingOrdersData
import com.denkiri.poscashier.models.pending.PendingTransaction
import com.denkiri.poscashier.models.pending.PendingTransactionData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.utils.OnProductClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_roll_back.*


/**
 * A simple [Fragment] subclass.
 * Use the [RollBackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RollBackFragment : Fragment() {
    lateinit var pendingOrdersAdapter: RollBackProductAdapter
    private lateinit var pendingOrders: ArrayList<PendingOrders>
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    private fun setStatusB(data: Resource<OrderData>) {
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
                //  viewModel.getOfflineOrders()
                // empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            //   empty_layout.visibility = View.VISIBLE
            ///   main.visibility = View.GONE
            //    empty_button.setOnClickListener({ init() })
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getPendingOrders()
        }
    }
    fun observers(){
        viewModel.observePendingOrders().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.orders != null && it.data.orders!!.isNotEmpty()) {
                    setUpInvoices(it.data.orders as ArrayList<PendingOrders>)
                } else {
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                    setUpInvoices(ArrayList())
                }
            }

        })
        viewModel.observeCartAction().observe(viewLifecycleOwner, {
            setStatusB(it)
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
    }

    private fun setUpInvoices(products: ArrayList<PendingOrders>) {
        this.pendingOrders = products
        pendingOrdersAdapter.refresh(this.pendingOrders)
        Handler().postDelayed(Runnable {
        }, 1)
    }

    private fun setStatus(data: Resource<PendingOrdersData>) {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roll_back, container, false)
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RollBackFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                RollBackFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    private fun initView(){
        pendingOrders= ArrayList()
        pendingOrdersAdapter = context?.let {
            RollBackProductAdapter(0, it,pendingOrders, object : OnProductClick {
                override fun selected(pos: Int) {
                    viewModel.deleteCartItem( pendingOrders[pos].transactionId.toString(), pendingOrders[pos].qty.toString(), pendingOrders[pos].product.toString(), pendingOrders[pos].invoice.toString())

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
        val filteredProductAry: ArrayList<PendingOrders> = ArrayList()
        val productAry : ArrayList<PendingOrders> = pendingOrders
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
}