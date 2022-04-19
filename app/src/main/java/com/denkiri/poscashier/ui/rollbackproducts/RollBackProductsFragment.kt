package com.denkiri.poscashier.ui.rollbackproducts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denkiri.poscashier.R

class RollBackProductsFragment : Fragment() {

    companion object {
        fun newInstance() = RollBackProductsFragment()
    }

    private lateinit var viewModel: RollBackProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.roll_back_products_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RollBackProductsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}