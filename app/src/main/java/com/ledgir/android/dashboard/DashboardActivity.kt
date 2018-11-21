package com.ledgir.android.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ledgir.android.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        viewModel.accountData.observe(this, Observer {
            text2.text = it.income.toString()
        })
        viewModel.userData.observe(this, Observer {
            text1.text = "${it.firstName} ${it.lastName}"
        })
    }
}