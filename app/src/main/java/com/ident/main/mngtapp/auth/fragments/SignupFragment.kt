package com.ident.main.mngtapp.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.auth.AuthActivity
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.dashboard.DashBoardActivity
import com.ident.main.mngtapp.databinding.FragmentSignupBinding
import com.ident.main.mngtapp.utils.UtilsFunctions


class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            btnCreateACC.setOnClickListener {
                if (isValidate()) {
                    (activity as AuthActivity).startActivity(
                        Intent(requireActivity(), DashBoardActivity::class.java)
                    )
                }
            }
            tvLogin.setOnClickListener {
                (activity as AuthActivity).supportFragmentManager.popBackStack()
            }
        }
    }

    private fun isValidate(): Boolean {
        viewDataBinding?.apply {
            return if (edtName.text.isNullOrEmpty()) {
                showToast(getString(R.string.valid_enter_name))
                false
            } else if (!UtilsFunctions.isValidEmail(edtEmailId.text.toString())) {
                showToast(getString(R.string.valid_email))
                false
            } else if (mMobileNo.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_valid_mobile_number))
                false
            } else if (edtPassword.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_password))
                false
            } else if (edtCnfrmPwd.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_password))
                false
            } else {
                true
            }
        }
        return true
    }


}