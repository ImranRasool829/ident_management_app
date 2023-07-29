package com.ident.main.mngtapp.patient

import android.os.Bundle
import android.view.View
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentPatientInformationBinding
import com.ident.main.mngtapp.model.PatientDataModel


class PatientInformationFragment(model: PatientDataModel) :
    BaseFragment<FragmentPatientInformationBinding>(
        FragmentPatientInformationBinding::inflate
    ) {

    val data: PatientDataModel = model
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI(data)
    }

    private fun updateUI(data: PatientDataModel) {

        viewDataBinding?.apply {
            tvPatientNI.text =data.name
            tvPatientPNumI.text =data.phone
            tvPatientEmailI.text =data.email
            tvPatientAddressI.text =data.address
            tvPatientGenderI.text =data.gender
            tvPatientAgeI.text =data.dob
            tvPatientReasonI.text =data.reason_to_come
            tvPatientAllergyI.text =data.allergies
            tvPatientSI.text =data.smoking
            tvPatientDI.text =data.drinking
            tvPatientTI.text =data.tobacco
            tvPatietDepartmentI.text =data.department_name


        }
    }

}