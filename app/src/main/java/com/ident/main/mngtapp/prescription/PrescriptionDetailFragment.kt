package com.ident.main.mngtapp.prescription

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentPrescriptionDetailBinding
import com.ident.main.mngtapp.databinding.ItemPrescriptionDetailBinding


class PrescriptionDetailFragment :
    BaseFragment<FragmentPrescriptionDetailBinding>(FragmentPrescriptionDetailBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            mCardViewAddMedicine.setOnClickListener {
                (activity as PrescriptionContainerActivity).replaceFragment(AddPrescriptionFragment(), true)
            }

            setPatientAdapter(mRVPrescription)
        }
    }

    private fun setPatientAdapter(recyclerView: RecyclerView) {

        val list = ArrayList<String>()
        list.add("A")
        list.add("B")

        val rvAdapProgress =
            RecyclerViewGenricAdapter<String, ItemPrescriptionDetailBinding>(list,
                R.layout.item_prescription_detail,
                object : RecyclerCallback<ItemPrescriptionDetailBinding, String> {
                    override fun bindData(
                        binder: ItemPrescriptionDetailBinding,
                        model: String,
                        position: Int,
                        itemView: View
                    ) {

                        //doStuff
                    }
                })
        recyclerView.adapter = rvAdapProgress

    }

}