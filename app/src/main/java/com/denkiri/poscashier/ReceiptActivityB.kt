package com.denkiri.poscashier

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.denkiri.poscashier.receipt.ui.mreceipt.ReceiptFragmentB
import kotlinx.android.synthetic.main.activity_receipt_b.*

class ReceiptActivityB : AppCompatActivity() {
    var invoice: String?=null
    var paymentMode:String?=null
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
        setContentView(R.layout.activity_receipt_b)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        invoice=intent.getStringExtra("id")
        paymentMode=intent.getStringExtra("paymentMode")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ReceiptFragmentB.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {

    }
}