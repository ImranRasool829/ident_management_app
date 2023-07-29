package com.ident.main.mngtapp.prescription

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityPrescriptionBinding

class PrescriptionContainerActivity :
    BaseActivity<ActivityPrescriptionBinding>(ActivityPrescriptionBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            toolBar.imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            toolBar1.imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            if (intent != null && intent.hasExtra(GlobalAppConstant.prescriptionDetail)) {
                llToolBar.visibility = View.GONE
                llToolBar1.visibility = View.VISIBLE
                toolBar1.tvTitle.text = resources.getString(R.string.prescription_detail)
                replaceFragment(PrescriptionDetailFragment(), false)

            } else if (intent != null && intent.hasExtra(GlobalAppConstant.addPrescription)) {
                llToolBar1.visibility = View.GONE
                llToolBar.visibility = View.VISIBLE
                toolBar.tvTitle.text = resources.getString(R.string.add_prescriptions)
                replaceFragment(AddPrescriptionFragment(), false)

            }

        }

    }

    fun replaceFragment(fragment: Fragment, isAdded: Boolean) {

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()

            if (isAdded) {
                transaction.addToBackStack(fragment::class.java.canonicalName)
            }
            transaction.replace(R.id.setPrescriptionContainer, fragment)

            transaction.commit()
        }
    }

}