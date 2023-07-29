package com.ident.main.mngtapp.billing

import android.os.Bundle
import android.view.View
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentBillingSummaryBinding
import com.ident.main.mngtapp.model.PatientDataModel


class BillingSummaryFragment(
    patientName: String, val date: String, service: String, serviceDescription: String,
    teethNumber: String, price: String, quantity: String, amount: String
) : BaseFragment<FragmentBillingSummaryBinding>(
    FragmentBillingSummaryBinding::inflate
) {
    private val patientName: String = patientName
    private val service: String = service
    private val description: String = serviceDescription
    private val teethNumber: String = teethNumber
    private val price: String = price
    private val quantity: String = quantity
    private val amount: String = amount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            tvDate.text = date
            tvPatientName.text = patientName
            tvService.text = service
            tvDescription.text = description
            tvTeethNumber.text = teethNumber
            tvPrice.text = price
            tvQuantity.text = quantity
            tvTotalAmount.text = amount
        }
    }
}