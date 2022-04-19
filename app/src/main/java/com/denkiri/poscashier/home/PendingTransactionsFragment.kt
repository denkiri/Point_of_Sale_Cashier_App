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
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.pending.PendingTransaction
import com.denkiri.poscashier.models.pending.PendingTransactionData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.utils.OnProductClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_pending_transactions.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [PendingTransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendingTransactionsFragment : Fragment() {
    lateinit var pendingTransactionAdapter: PendingTransactionAdapter
    private lateinit var pendingTransactions: ArrayList<PendingTransaction>
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_transactions, container, false)
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
    private fun setStatusB(data: Resource<TransactionData>) {
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
    private fun setStatus(data: Resource<PendingTransactionData>) {
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
                  empty_text.text = data.message
                 view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }


              empty_layout.visibility = View.VISIBLE
             main.visibility = View.GONE
            empty_button.setOnClickListener { init() }
        }
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getPendingTransactions()
        }
    }
    fun observers(){
        viewModel.observePendingTransaction().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.orders != null && it.data.orders!!.isNotEmpty()) {
                    setUpInvoices(it.data.orders as ArrayList<PendingTransaction>)
                } else {
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                    setUpInvoices(ArrayList())
                }
            }

        })
        viewModel.observeTransactionData().observe(
            viewLifecycleOwner,
            {
                setStatusB(it)
                if (it.status == Status.SUCCESS) {
                    Toasty.success(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()
                    init()
                }
                if (it.status == Status.ERROR) {
                    init()
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()
                }
            })
    }

    private fun setUpInvoices(products: ArrayList<PendingTransaction>) {
        this.pendingTransactions = products
        pendingTransactionAdapter.refresh(this.pendingTransactions)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PendingTransactionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                PendingTransactionsFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    fun filter(text: String) {
        val filteredProductAry: ArrayList<PendingTransaction> = ArrayList()
        val productAry : ArrayList<PendingTransaction> = pendingTransactions
        for (eachProduct in productAry) {
            if (eachProduct.phoneNumber!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredProductAry.add(eachProduct)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        pendingTransactionAdapter.filterList(filteredProductAry)
        pendingTransactions=filteredProductAry

        pendingTransactionAdapter.notifyDataSetChanged()
        if (pendingTransactionAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
            Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            observers()

        }
        else{
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }
    private fun initView(){
        pendingTransactions= ArrayList()
        pendingTransactionAdapter = context?.let {
            PendingTransactionAdapter(0, it,pendingTransactions, object : OnProductClick {
                override fun selected(pos: Int) {
                    val intent = Intent(activity, ReceiptActivityB::class.java)
                    intent.putExtra("id",pendingTransactions[pos].invoiceNumber)
                    intent.putExtra("paymentMode",pendingTransactions[pos].type)
                    startActivity(intent)

                      }

                override fun onClickListener(position1: Int) {
                    viewModel.pendingMpesaSale(pendingTransactions[position1].amount,pendingTransactions[position1].phoneNumber,pendingTransactions[position1].invoiceNumber)



                }




            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = pendingTransactionAdapter
        pendingTransactionAdapter.notifyDataSetChanged()
    }
}