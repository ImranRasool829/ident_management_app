package com.ident.main.mngtapp.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentLoginBinding
import com.ident.main.mngtapp.databinding.FragmentProfileBinding
import com.ident.main.mngtapp.model.UserDataM
import com.ident.main.mngtapp.utils.SharedPrefClass

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            editProfileTV.setOnClickListener {
                (activity as SettingActivity).replaceFragment(EditProfileFragment(), true)
            }
        }

        updateUI()
    }

    private fun updateUI() {
        val model: String =
            SharedPrefClass().getPrefValue(requireContext(), GlobalAppConstant.USER_DATA).toString()
        val data: UserDataM = Gson().fromJson(model, UserDataM::class.java)

        viewDataBinding?.apply {
            if (data != null) {
                setImageWithUrl(data.profile_pics, imgProiflePIC)
                tvNameNav.text = data.name
                tvEmailNav.text = data.email
                tvMobileNo.text = data.phone

            }

        }

    }

}