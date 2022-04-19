package com.denkiri.poscashier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.denkiri.poscashier.home.RollBackFragmentB
import kotlinx.android.synthetic.main.roll_back_activity.*

class RollBackActivity : AppCompatActivity() {
    var invoice: String?=null
    var paymentType:String?=null
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roll_back_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        invoice=intent.getStringExtra("id")
        paymentType=intent.getStringExtra("paymentType")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RollBackFragmentB.newInstance())
                .commitNow()
        }
    }
}