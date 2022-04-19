package com.denkiri.poscashier.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denkiri.poscashier.R
import com.denkiri.poscashier.ui.ui.auth.LoginFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow()
        }
    }
}