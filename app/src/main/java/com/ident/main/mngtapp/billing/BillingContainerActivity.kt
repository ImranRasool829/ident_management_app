package com.ident.main.mngtapp.billing

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityBillingContainerBinding

class BillingContainerActivity :
    BaseActivity<ActivityBillingContainerBinding>(ActivityBillingContainerBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {

            toolBar.imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            replaceFragment(
                AddBillingFragment(),
                false,
                resources.getString(R.string.create_invoice)
            )
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
            transaction.replace(R.id.setBillingContainer, fragment)

            transaction.commit()
        }
    }

}