package com.ident.main.mngtapp.appointment.fragment

import android.os.Bundle
import android.view.View
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentAddAppointmentBinding


class EditAppointmentFragment : BaseFragment<FragmentAddAppointmentBinding>(
    FragmentAddAppointmentBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

        }

    }
}