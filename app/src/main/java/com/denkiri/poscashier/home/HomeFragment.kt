package com.denkiri.poscashier.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.poscashier.R
import com.denkiri.poscashier.adapters.ProductAdapter
import com.denkiri.poscashier.models.order.CartData
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.storage.PreferenceManager
import com.denkiri.poscashier.utils.BadgeView.BadgeView
import com.denkiri.poscashier.utils.OnProductClick
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {
    lateinit var productAdapter: ProductAdapter
    private lateinit var products: ArrayList<Product>
    var invoice:String?=null
    lateinit var qBadgeView: BadgeView
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
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
    fun filter(text: String) {
        val filteredProductAry: ArrayList<Product> = ArrayList()
        val productAry : ArrayList<Product> = products
        for (eachProduct in productAry) {
            if (eachProduct.productName!!.toLowerCase().contains(text.toLowerCase()) ) {
                filteredProductAry.add(eachProduct)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        productAdapter.filterList(filteredProductAry)
        products=filteredProductAry

        productAdapter.notifyDataSetChanged()
        if (productAdapter.itemCount==0){
            recyclerView.visibility=View.VISIBLE
          //  Toasty.error(requireContext(), "No Matching Search Results", Toast.LENGTH_SHORT, true).show()
            val snack = Snackbar.make(requireView(),"No Matching Search Results",Snackbar.LENGTH_LONG)
            snack.setAction("Refresh") {
               viewModel.getOfflineProducts()
            }
            snack.show()

            viewModel.observeOfflineProducts().observe(viewLifecycleOwner, {


                    setUpProducts(it.data as ArrayList<Product>)
            })


        }
        else{
            // Toasty.success(requireContext(),"Matching Search Results", Toast.LENGTH_SHORT, true).show()
            recyclerView.visibility=View.VISIBLE


        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        qBadgeView = BadgeView(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView!!, dx, dy)
                if (dy > 0 && fab.visibility === View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility !== View.VISIBLE) {
                    fab.show()
                }
            }
        })

    //  init()
      viewModel.getOfflineProducts()
       viewModel.getOfflineOrders()
        initView()
        invoice()
        observers()
        fab.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, CartFragment())
            .commitNow()


        }

        card_viewF.setOnClickListener {
           requireActivity().supportFragmentManager.beginTransaction()
          .replace(R.id.container, PendingTransactionsFragment())
           .commitNow()
               }
        card_viewG.setOnClickListener {   requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, RecordIncomeFragment())
            .commitNow()  }


           card_viewE.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, RollBackFragment())
            .commitNow() }
        card_viewD.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, SalesFragment())
            .commitNow() }
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
    fun invoice(){
        if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
            invoice= viewModel.getRandPassword(8)
            viewModel.saveInvoiceNumber("RS-"+invoice!!)

         //   view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
        }
        else{
          //  view?.let { Snackbar.make(it,PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }
        }
    }
    fun init() {



            if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
                viewModel.getOfflineProducts()
                invoice= viewModel.getRandPassword(8)
            viewModel.saveInvoiceNumber(invoice!!)
                viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
                viewModel.getProducts()
          //  view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
        }
            else{
                viewModel.getOfflineProducts()
                viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
                viewModel.getProducts()
              //  view?.let { Snackbar.make(it,PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }
            }

    }
    fun initB() {
        if (NetworkUtils.isConnected(requireContext())) {
            viewModel.getProducts()

            if (PreferenceManager(requireContext()).getInvoiceNumber()==""){
                invoice= viewModel.getRandPassword(8)
                viewModel.saveInvoiceNumber(invoice!!)
                viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
               // view?.let { Snackbar.make(it, invoice!!, Snackbar.LENGTH_LONG).show() }
            }
            else{
                viewModel.getOrder(PreferenceManager(requireContext()).getInvoiceNumber())
                //view?.let { Snackbar.make(it,PreferenceManager(requireContext()).getInvoiceNumber(), Snackbar.LENGTH_LONG).show() }
            }
        }
    }
    private fun setStatus(data: Resource<ProductData>) {
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
                viewModel.getOfflineProducts()
                viewModel.getOfflineOrders()
                //  empty_text.text = data.message
                // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

        }
    }
    private fun setStatusB(data: Resource<CartData>) {
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
                // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }
            viewModel.observeOfflineOrders()

         //   empty_layout.visibility = View.VISIBLE
           // main.visibility = View.GONE
            //empty_button.setOnClickListener({ init() })
        }
    }
    private fun setUpProducts(products: ArrayList<Product>) {
        this.products = products
        productAdapter.refresh(this.products)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    private fun initView(){
        products = ArrayList()
        productAdapter = context?.let {
            ProductAdapter(0, it, products, object : OnProductClick {
                override fun selected(pos: Int) {
                    val args = Bundle()
                 //   args.putDouble("price", products[pos].price)
                    args.putString("productName", products[pos].productName)
                    args.putString("key", "value")
                    val bottomSheet = AddToCartFragment()
                    bottomSheet.setArguments(args)
                    bottomSheet.show((context as FragmentActivity).supportFragmentManager, bottomSheet.tag)
                }

                override fun onClickListener(position1: Int) {
                    if (products[position1].qtyLeft!! >0){
                    val args = Bundle()
                        args.putString("productCode", products[position1].productCode)
                        args.putString("productId", products[position1].productId.toString())
                        args.putString("price", products[position1].price.toString())
                        args.putString("cost", products[position1].cost.toString())
                        args.putString("vat", products[position1].vat)
                        args.putString("expirationDate", products[position1].expirationDate)
                        args.putString("productName", products[position1].productName)
                        args.putString("descriptionName", products[position1].descriptionName)
                        args.putString("category", products[position1].category)
                        args.putString("brand", products[position1].brand)
                        args.putString("quantityLeft", products[position1].qtyLeft.toString())
                        args.putString("invoiceNumber",PreferenceManager(requireContext()).getInvoiceNumber())
                    val bottomSheet = AddToCartFragment()
                        bottomSheet.arguments = args
                        bottomSheet.isCancelable = false
                    bottomSheet.show((context as FragmentActivity).supportFragmentManager, bottomSheet.tag)
                }

                }




            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
    }
    fun observers(){
        viewModel.observeProducts().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.products != null && !it.data.products!!.isEmpty()) {
                    setUpProducts(it.data.products as ArrayList<Product>)
                } else {
                    setUpProducts(ArrayList())
                }
            }

        })
        viewModel.observeCartData().observe(viewLifecycleOwner, {
            setStatusB(it)
            if (it.status == Status.SUCCESS) {
                viewModel.saveTotal(it.data!!)



            }

        })
        viewModel.observeOfflineOrders().observe(viewLifecycleOwner, {
            if (it.data.isNullOrEmpty()){
                //  init()

            }})
        viewModel.observeOfflineProducts().observe(viewLifecycleOwner, {
        if (it.data.isNullOrEmpty()){
         //   init()

        }
            else{
      setUpProducts(it.data as ArrayList<Product>)}
       })
    }
    private fun setCartCount(count: Int?) {
        qBadgeView.bindTarget(fab).badgeText = "" + count
    }

    fun refreshBadge() {
        setCartCount(0)
        viewModel.observeOfflineOrders().observe(viewLifecycleOwner, {
            if (it.data!=null){
                    setCartCount(it.data.size)
            }
        })
        // it: implicit name of a single parameter
        //  viewModel.getCartCount().observe(this, Observer {

        //   if (it != null)
        //       setCartCount(it.items_quantity)
        // })
    }
    override fun onStart() {
        super.onStart()
        refreshBadge()
    }

    override fun onPause() {
        super.onPause()
        refreshBadge()
    }

    override fun onResume() {
        super.onResume()
        refreshBadge()

    }

}