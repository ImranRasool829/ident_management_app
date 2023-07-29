package com.ident.main.mngtapp.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityBookAppointmentBinding

class BookAppointmentActivity : BaseActivity<ActivityBookAppointmentBinding>(ActivityBookAppointmentBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            lltoolBar.imgBack.setOnClickListener {
                finish()
            }
            lltoolBar.tvTitle.text = resources.getString(R.string.book_appointment)
        }
    }
}