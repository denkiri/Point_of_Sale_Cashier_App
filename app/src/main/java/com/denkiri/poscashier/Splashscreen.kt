package com.denkiri.poscashier

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.denkiri.poscashier.storage.PreferenceManager
import com.denkiri.poscashier.ui.AuthActivity
import com.denkiri.poscashier.ui.ui.auth.AuthViewModel

class Splashscreen : AppCompatActivity() {
    private val time:Long=3000 // 3 sec
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        Handler(Looper.getMainLooper()).postDelayed({

            init()
        }, time)
    }

    fun  init() {
        if(PreferenceManager(this).getLoginStatus()==0) {

            startActivity(Intent(this@Splashscreen, AuthActivity::class.java))
            finish()
        }
        else{
            startActivity(Intent(this@Splashscreen,MainActivity::class.java))
            finish()
        }
    }

}