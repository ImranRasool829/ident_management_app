package com.ident.main.mngtapp.auth

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.fragment.app.Fragment
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.auth.fragments.LoginFragment
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(LoginFragment(), false)

    }

    fun replaceFragment(fragment: Fragment, isAdded: Boolean) {

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()

            if (isAdded) {
                transaction.addToBackStack(fragment::class.java.canonicalName)
            }
            transaction.replace(R.id.authContainer, fragment)

            transaction.commit()
        }
    }


    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        return super.getOnBackInvokedDispatcher()

    }


}