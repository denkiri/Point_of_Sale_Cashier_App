package com.denkiri.poscashier.home

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.poscashier.R
import com.denkiri.poscashier.adapters.CartAdapter
import com.denkiri.poscashier.adapters.ProductAdapter
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.order.CartData
import com.denkiri.poscashier.models.order.Order
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.receipt.ReceiptActivity
import com.denkiri.poscashier.storage.PreferenceManager
import com.denkiri.poscashier.utils.BadgeView.BadgeView
import com.denkiri.poscashier.utils.OnCartItemClick
import com.denkiri.poscashier.utils.OnProductClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.avi
import kotlinx.android.synthetic.main.fragment_cart.empty_iconB
import kotlinx.android.synthetic.main.fragment_cart.empty_layout
import kotlinx.android.synthetic.main.fragment_cart.empty_layoutB
import kotlinx.android.synthetic.main.fragment_cart.empty_text
import kotlinx.android.synthetic.main.fragment_cart.main
import kotlinx.android.synthetic.main.fragment_cart.recyclerView
import kotlinx.android.synthetic.main.fragment_checkout.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    var radio: RadioButton?=null
    lateinit var cartAdapter: CartAdapter
    private lateinit var order: ArrayList<Order>
    var invoice:String?=null
    var totalAmount:String?="0"
    var customer:String?=null
    var cashier:String?=null
    var businessCode:String?=null
    var amount:String?="0"
    var dueDates:String?=null
    var id:String?=null
    private lateinit var viewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                CartFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioPaymentType.setOnCheckedChangeListener { _, checkedId ->
            radio = view.findViewById(checkedId)
        }
        var id: Int = radioPaymentType.checkedRadioButtonId
        if (id!=-1){ // If any radio button checked from radio group
            // Get the instance of radio button using id
            radio = view.findViewById(id)


        }else{
            // If no radio button checked in this radio group
            Toast.makeText(context,"Select Payment Method",
                Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        id=PreferenceManager(requireContext()).getInvoiceNumber()
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
        viewModel.getOfflineOrders()
        viewModel.getTotalAmount()
    //   init()
      //  viewModel.getOfflineOrders()
     //   viewModel.getTotalAmount()
        initView()
        setUpUi()
        observers()
        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitNow()
        }
      empty_iconB.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitNow() }
     btnCheckOut.setOnClickListener {
         if(radio?.text.toString()=="cash"){
             checkSubscription()


         }
         else {
             val args = Bundle()
             args.putString("paymentType", radio?.text.toString())
             args.putString("amount", totalAmount.toString())
             val bottomSheet = CheckoutFragment()
             bottomSheet.arguments = args
             bottomSheet.isCancelable = false
             bottomSheet.show((context as FragmentActivity).supportFragmentManager, bottomSheet.tag)
         }
     }

    }
    fun saveCashSale() {

        viewModel.saveCashSale(PreferenceManager(requireContext()).getInvoiceNumber(), cashier.toString(), radio?.text.toString(), amount.toString(), dueDates.toString(), amount.toString(), customer.toString())
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
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
    fun init() {
            if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
                invoice= viewModel.getRandPassword(8)
                viewModel.saveInvoiceNumber(invoice!!)
                viewModel.getTotalAmount()
                viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
              //  view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
            }
            else{
                viewModel.getTotalAmount()
                viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
              // view?.let { Snackbar.make(it, PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }

            }

    }
    private fun setStatus(data: Resource<CartData>) {
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
                viewModel.getOfflineOrders()

            }
        }
    }
    private fun setStatusD(data: Resource<TransactionData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            btnCheckOut.isEnabled = false
            btnCheckOut.isClickable = false
            btnCheckOut.text=getString(R.string.save)
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            btnCheckOut.isEnabled = true
            btnCheckOut.isClickable = true
            btnCheckOut.text=getString(R.string.save_receipt)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }

            }
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
    private fun setUpOrders(order: ArrayList<Order>) {
        this.order=order
        cartAdapter.refresh(this.order)
        totalItemsValue.text= order.size.toString()
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun initView(){
        order = ArrayList()
        cartAdapter = context?.let {
            CartAdapter(0, it, order, object : OnCartItemClick {

                override fun delete(pos: Int) {
                    viewModel.deleteCartItem(order[pos].transactionId.toString(),order[pos].qty.toString(),order[pos].product.toString(),order[pos].invoice.toString())
                }
                override fun increase(pos: Int) {
                    viewModel.increaseCartItem(order[pos].invoice.toString(),order[pos].product.toString(),order[pos].qty.toString(),order[pos].commissionValue.toString(),order[pos].vatValue.toString(),order[pos].discountValue.toString(),order[pos].price.toString(),order[pos].costValue.toString(),order[pos].expiry.toString(),order[pos].name.toString(),order[pos].dname.toString(),order[pos].category.toString(),order[pos].qtyleft.toString(),order[pos].productId.toString())
                }
                override fun decrease(pos: Int) {
                    if (order[pos].qty!!.toInt()==1){
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Remove Cart Item")
                        builder.setMessage("Are you sure you want to remove?")
                        builder.setPositiveButton("Yes") { dialog, _ ->
                            dialog.dismiss()
                            viewModel.deleteCartItem(order[pos].transactionId.toString(),order[pos].qty.toString(),order[pos].product.toString(),order[pos].invoice.toString())
                        }
                        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    }
                    else {
                        viewModel.decreaseCartItem(order[pos].invoice.toString(), order[pos].product.toString(), order[pos].qty.toString(), order[pos].commissionValue.toString(), order[pos].vatValue.toString(), order[pos].discountValue.toString(), order[pos].price.toString(), order[pos].costValue.toString(), order[pos].expiry.toString(), order[pos].name.toString(), order[pos].dname.toString(), order[pos].category.toString(), order[pos].qtyleft.toString(), order[pos].productId.toString())
                    }
                }

                override fun onClickListener(position1: Int) {
                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()
    }
    fun observers(){
    viewModel.observeOfflineOrders().observe(viewLifecycleOwner, {
          if (it.data.isNullOrEmpty()){
             // button.visibility=View.VISIBLE
             init()
              main.visibility = View.GONE
              empty_layoutB.visibility=View.VISIBLE
         }
            else{
             setUpOrders(it.data as ArrayList<Order>)}
       })
        viewModel.observeCartAction().observe(viewLifecycleOwner, {
            setStatusB(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let { it1 -> viewModel.saveProducts(it1) }
                it.data?.let { it1 -> viewModel.saveOrders(it1) }
             //   it.data?.let { it1 -> viewModel.saveTotalAmount(it1) }
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {

                    setUpOrders(it.data.order as ArrayList<Order>)

                } else {
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                    setUpOrders(ArrayList())

                }
            }

        })
        viewModel.observeCartData().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                //   it.data?.let { it1 -> viewModel.saveTotalAmount(it1) }
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {

                   setUpOrders(it.data.order as ArrayList<Order>)


                } else {
                    setUpOrders(ArrayList())
                    main.visibility = View.GONE
                    empty_layoutB.visibility=View.VISIBLE
                }
            }

        })
        viewModel.getTotalAmount().observe(viewLifecycleOwner, {
            try {
                totalAmount=it.totalAmount.toString()
                amount=it.totalAmount.toString()
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.currency = Currency.getInstance("KSh")
               totalAmountValue.text =format.format(it.totalAmount).toString()

            } catch (e: Exception) {
            }
        })
        viewModel.observeSubscriptionAction().observe( viewLifecycleOwner,
                {
                    setStatusC(it)
                    if (it.status == Status.SUCCESS) {

                        if (it.data?.status ==1 && it.data.error==false) {
                            //  it.data?.message?.let { it1 -> Toasty.success(requireContext(), it1, Toast.LENGTH_LONG,false).show() }
                            saveCashSale()
                        } else {
                            //  Toasty.error(requireContext(),"Subscribe", Toast.LENGTH_LONG,false).show()

                        }
                    }


                }

        )
        viewModel.observeTransactionData().observe(
                viewLifecycleOwner,
                {
                    setStatusD(it)
                    if (it.status == Status.SUCCESS) {
                        viewModel.saveInvoiceNumber("")
                        val intent = Intent(activity, ReceiptActivity::class.java)
                        intent.putExtra("id",id)
                        intent.putExtra("paymentMode",radio?.text.toString())
                        startActivity(intent)

                    }
                    if (it.status == Status.ERROR) {
                        Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()
                    }
                })


    }
    private fun checkSubscription(){
        viewModel.checkSubscription(businessCode.toString())
    }
    private fun setStatusC(data: Resource<PaymentResponseData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            btnCheckOut.visibility = View.VISIBLE
            btnCheckOut.isEnabled = false
            btnCheckOut.isClickable = false
            btnCheckOut.text=getString(R.string.save)
            btnCheckOut.setBackgroundColor(resources.getColor(R.color.divider))
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            btnCheckOut.isEnabled = true
            btnCheckOut.isClickable = true
            btnCheckOut.text=getString(R.string.save_receipt)
            btnCheckOut.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
              if (status == Status.ERROR) {
            if (data.message != null) {
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


}
