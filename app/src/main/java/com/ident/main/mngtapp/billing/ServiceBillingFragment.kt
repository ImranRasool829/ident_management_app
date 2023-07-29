package com.ident.main.mngtapp.billing

import android.os.Bundle
import android.view.View
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentPrescriptionServiceBinding

class ServiceBillingFragment : BaseFragment<FragmentPrescriptionServiceBinding>(
    FragmentPrescriptionServiceBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

        }
    }

}