package com.ident.main.mngtapp.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.billing.BillingHistoryFragment
import com.ident.main.mngtapp.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {
            toolBar.imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            if (intent != null && intent.hasExtra(GlobalAppConstant.aboutUS)) {
                val bundle: Bundle? = intent.extras
                val aboutUs: String? = intent.getStringExtra(GlobalAppConstant.aboutUS)
                toolBar.tvTitle.text = resources.getString(R.string.about_us)
                replaceFragment(AboutUsFragment(), false)
            } else if (intent.hasExtra(GlobalAppConstant.feedback)) {
                toolBar.tvTitle.text = resources.getString(R.string.feedback)
                replaceFragment(FeedbackFragment(), false)
            } else if (intent.hasExtra(GlobalAppConstant.changePassword)) {
                toolBar.tvTitle.text = resources.getString(R.string.change_password)
                replaceFragment(ChangePasswordFragment(), false)
            } else if (intent.hasExtra(GlobalAppConstant.billingHistory)) {
                toolBar.tvTitle.text = resources.getString(R.string.billing_history)
                replaceFragment(BillingHistoryFragment(), false)
            } else {
                toolBar.tvTitle.text = resources.getString(R.string.profile)
                replaceFragment(ProfileFragment(), false)
            }

        }

    }

    fun replaceFragment(fragment: Fragment, isAdded: Boolean) {

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()

            if (isAdded) {
                transaction.addToBackStack(fragment::class.java.canonicalName)
            }
            transaction.replace(R.id.setContainer, fragment)

            transaction.commit()
        }
    }


}