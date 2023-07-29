package com.ident.main.mngtapp.patient

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentPatientProfileBinding
import com.ident.main.mngtapp.databinding.ItemPrescriptionDetailBinding
import com.ident.main.mngtapp.databinding.ItemProfileMediDoseBinding

class PatientProfileFragment(patientID: String) : BaseFragment<FragmentPatientProfileBinding>(
    FragmentPatientProfileBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            setAdapter(mRVDetail)
        }
    }

    private fun setAdapter(recyclerView: RecyclerView) {

        val list = ArrayList<String>()
        list.add("Ciplox")
        list.add("Rofza")
        list.add("Shampoo")


        val rvAdapProgress =
            RecyclerViewGenricAdapter<String, ItemProfileMediDoseBinding>(list,
                R.layout.item_profile_medi_dose,
                object : RecyclerCallback<ItemProfileMediDoseBinding, String> {
                    override fun bindData(
                        binder: ItemProfileMediDoseBinding,
                        model: String,
                        position: Int,
                        itemView: View
                    ) {
                        binder.apply {
                            tvTabName.text = model
                        }
                    }
                })
        recyclerView.adapter = rvAdapProgress

    }


}