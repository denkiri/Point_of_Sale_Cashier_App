package com.denkiri.poscashier.ui.ui.auth

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.poscashier.MainActivity
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.oauth.Oauth
import com.denkiri.poscashier.models.oauth.Profile
import com.denkiri.poscashier.utils.Validator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }
    private fun validate(): Boolean {
        if (!Validator.isValidName(username)) {
            return false
        }

        return true
    }
    private fun setStatus(data: Resource<Oauth>) {
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
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }
        }
    }
    private fun getParameters(): Oauth {

        return Oauth(Profile(username.text.toString(), password.text.toString()))
    }


    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        signIn.setOnClickListener { signIn() }
        viewModel.observeSignIn().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
                if (data.status == Status.SUCCESS && data.data != null) {

                    viewModel.saveProfile(data.data)
                  //  viewModel.getProducts()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
            }
        })
        // TODO: Use the ViewModel
    }
    fun signIn(){
        if (validate()) {
            viewModel.signIn(getParameters())
        }


    }

}