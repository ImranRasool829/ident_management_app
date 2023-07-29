package com.ident.main.mngtapp.patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityPatientContainerBinding
import com.ident.main.mngtapp.model.PatientDataModel


class PatientContainerActivity :
    BaseActivity<ActivityPatientContainerBinding>(ActivityPatientContainerBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {
            toolBar.imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            if (intent != null && intent.hasExtra(GlobalAppConstant.patientDetail)) {
                val data: String? = intent.getStringExtra(GlobalAppConstant.patientDetail)
                val model: PatientDataModel = Gson().fromJson(data, PatientDataModel::class.java)
                toolBar.tvTitle.text = resources.getString(R.string.patient_information)
                replaceFragment(PatientInformationFragment(model), false)

            } else if (intent != null && intent.hasExtra(GlobalAppConstant.patientEdit)) {
                val data: String? = intent.getStringExtra(GlobalAppConstant.patientEdit)
                val model: PatientDataModel = Gson().fromJson(data, PatientDataModel::class.java)
                toolBar.tvTitle.text = resources.getString(R.string.edit_details)
                replaceFragment(EditPatientFragment(model), false)

            } else if (intent != null && intent.hasExtra(GlobalAppConstant.patientID)) {
                val patientID: String = intent.getStringExtra(GlobalAppConstant.patientID).toString()
                toolBar.tvTitle.text = resources.getString(R.string.patient_profile)
                replaceFragment(PatientProfileFragment(patientID), false)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, isAdded: Boolean) {

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()

            if (isAdded) {
                transaction.addToBackStack(fragment::class.java.canonicalName)
            }
            transaction.replace(R.id.patientContainer, fragment)

            transaction.commit()
        }
    }

}