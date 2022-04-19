package com.denkiri.poscashier
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denkiri.poscashier.home.HomeFragment
import com.denkiri.poscashier.home.HomeViewModel
import com.denkiri.poscashier.storage.PharmacyDatabase
import com.denkiri.poscashier.storage.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        signupback.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")

            builder.setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                PreferenceManager(this).setLoginStatus(0)
                finish()
                startActivity(Intent(this, Splashscreen::class.java))
            }

            builder.setNegativeButton("No", { dialog, which -> dialog.dismiss() })
            val alert = builder.create()
            alert.show()

        }
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        supportFragmentManager.beginTransaction().replace(androidx.navigation.ui.R.id.container,
            HomeFragment.newInstance()).commitNow()
    }
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is HomeFragment) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")

            builder.setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                PreferenceManager(this).setLoginStatus(0)
                finish()
                startActivity(Intent(this, Splashscreen::class.java))
            }

            builder.setNegativeButton("No", { dialog, which -> dialog.dismiss() })
            val alert = builder.create()
            alert.show()            //additional code
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}