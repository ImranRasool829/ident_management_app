package com.ident.main.mngtapp.patient

import android.os.Bundle
import android.view.View
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentEditPatientBinding
import com.ident.main.mngtapp.model.PatientDataModel

class EditPatientFragment(model: PatientDataModel) :
    BaseFragment<FragmentEditPatientBinding>(FragmentEditPatientBinding::inflate) {

    val data: PatientDataModel = model

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI(data)

    }

    private fun updateUI(data: PatientDataModel) {

        viewDataBinding?.apply {
            if (data != null) {
                edtPEditName.setText(data.name)
                edtPEditNumber.setText(data.phone)
                edtPEditReason.setText(data.reason_to_come)
                edtPEditAllergy.setText(data.allergies)
            }
        }
    }


}