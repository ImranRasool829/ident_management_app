package com.ident.main.mngtapp.appointment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.appointment.fragment.AddAppointmentFragment
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.billing.AddBillingFragment
import com.ident.main.mngtapp.databinding.ActivityAppointmentBinding
import com.ident.main.mngtapp.prescription.PrescriptionDetailFragment

class
AppointmentContainerActivity :
    BaseActivity<ActivityAppointmentBinding>(ActivityAppointmentBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            toolBar.imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            if (intent != null && intent.hasExtra(GlobalAppConstant.editAppointment)) {
               // val bundle: Bundle? = intent.extras
                val edit: String? = intent.getStringExtra(GlobalAppConstant.editAppointment)
                val data: String? = intent.getStringExtra(GlobalAppConstant.appointmentData)

                replaceFragment(
                    AddAppointmentFragment(edit,data),
                    false,
                    resources.getString(R.string.edit_appointment)
                )

            } else {
                replaceFragment(
                    AddAppointmentFragment("ADD","nullData"), false, resources.getString(R.string.add_appointment)
                )

            }

        }
    }

    fun replaceFragment(fragment: Fragment, isAdded: Boolean, title: String) {

        viewDataBinding?.apply {
            toolBar.tvTitle.text = title
        }
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()

            if (isAdded) {
                transaction.addToBackStack(fragment::class.java.canonicalName)
            }
            transaction.replace(R.id.setAppointmentContainer, fragment)

            transaction.commit()
        }
    }

}