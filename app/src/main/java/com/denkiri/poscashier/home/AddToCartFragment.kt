package com.denkiri.poscashier.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.storage.PreferenceManager
import com.denkiri.poscashier.utils.Validator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import java.text.NumberFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddToCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddToCartFragment : RoundedBottomSheetDialogFragment() {
    var count = 1
    var productCode:String?=null
    var priceValue:String?=null
    var cost:String?=null
    var expirationDate:String?=null
    var descriptionName:String?=null
    var category:String?=null
    var brand:String?=null
    var quantityLeft:String?=null
    var invoiceNumber:String?=null
    var productId:String?=null
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_cart, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddToCartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AddToCartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setUpUi()
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val bundle = arguments
        productName.text=bundle?.getString("productName").toString()
        quantityleft.text=bundle?.getString("quantityLeft").toString()
        price.text=format.format(bundle?.getString("price")?.toDouble()).toString()
        vat.setText((bundle?.getString("vat")).toString())
        priceValue= bundle?.getString("price").toString()
        productCode=bundle?.getString("productCode").toString()
        productId=bundle?.getString("productId").toString()
        cost=bundle?.getString("cost").toString()
        expirationDate=bundle?.getString("expirationDate").toString()
        descriptionName=bundle?.getString("descriptionName").toString()
        category=bundle?.getString("category").toString()
        if(bundle?.getString("brand").toString()!="null"){
            brand=bundle?.getString("brand").toString()
            productBrand.text=bundle?.getString("brand").toString()
        }
        else{
            brand=bundle?.getString("brand").toString()
            productBrand.text=""
        }

        quantityLeft=bundle?.getString("quantityLeft").toString()
        invoiceNumber= PreferenceManager(requireContext()).getInvoiceNumber()
        add.setOnClickListener {
            count++
            qty.setText(count.toString())}
        remove.setOnClickListener {
            if (qty.text.toString()>"1"){
            count--
            qty.setText(count.toString())}
        }
        cancel.setOnClickListener {
            dismiss()
        }
        addToCart.setOnClickListener { addOrder() }
    }

    private fun validate(): Boolean {
        if (!Validator.isValidAmount(qty)) {
            return false
        }
        if(qty.text.toString()=="0"){
            Toasty.error(requireContext(),"Enter Valid Quantity", Toast.LENGTH_LONG, true).show()
            return false

        }
        if (!Validator.isValidAmount(discountValue)) {
            return false
        }
        if (!Validator.isValidAmount(vat)) {
            return false
        }
        /*
        if (discountValue.text.toString()>priceValue.toString()) {
            Toasty.error(requireContext(),"Discount Should be Lower Price", Toast.LENGTH_LONG, true).show()
            return false
        }
        if (discountValue.text.toString()>priceValue.toString()) {
            Toasty.error(requireContext(),"Discount Should be Lower Price", Toast.LENGTH_LONG, true).show()
            return false
        }

         */
        if (vat.text.toString().toInt()>20) {
            Toasty.error(requireContext(),"Enter correct Vat", Toast.LENGTH_LONG, true).show()
            return false
        }
     //   if (qty.text.toString()>quantityLeft.toString()){
           // Toasty.error(requireContext(),"Excess Quantity", Toast.LENGTH_LONG, true).show()
        //    return false
     //   }
        if (!Validator.isValidAmount(increasePrice)) {
            return false
        }
        return true
    }
    fun addOrder(){
        if (validate()){
            viewModel.addOrder(invoiceNumber.toString(),productCode.toString(),qty.text.toString(),increasePrice.text.toString(),vat.text.toString(),discountValue.text.toString(),priceValue.toString(),cost.toString(),expirationDate.toString(),productName.text.toString(),descriptionName.toString(),category.toString(),quantityLeft.toString(),productId.toString(),brand.toString())
        }
    }
    private fun status(data: Resource<OrderData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            cancel.visibility=View.INVISIBLE
            addToCart.isEnabled = false
            addToCart.isClickable = false
            addToCart.setBackgroundColor(resources.getColor(R.color.divider))
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            addToCart.isEnabled = true
            addToCart.isClickable = true
            addToCart.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                Toasty.error(requireContext(),data.message.toString(), Toast.LENGTH_LONG, true).show()
                cancel.visibility=View.VISIBLE
            }

        }
    }
    fun setUpUi() {
        viewModel.observeAddOrderAction().observe(viewLifecycleOwner, {
            status(it)

            if (it.status == Status.SUCCESS) {
                if (it.data?.order != null && it.data.order!!.isNotEmpty()) {
                 //   Toasty.success(requireContext(), it.data.message.toString(), Toast.LENGTH_LONG, true).show()
                    //  val navController = Navigation.findNavController(requireView())
                    //  navController.navigate(R.id.nav_product)
                    viewModel.saveProducts(it.data)
                    viewModel.saveOrders(it.data)
                 //   viewModel.saveTotalAmount(it.data)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment())
                            .commitNow()

                    dismiss()


                }
                else{
                    Toasty.error(requireContext(),it.message.toString(), Toast.LENGTH_LONG,false).show()

                }

            }

        })
    }


}