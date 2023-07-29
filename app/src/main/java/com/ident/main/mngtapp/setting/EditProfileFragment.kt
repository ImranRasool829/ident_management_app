package com.ident.main.mngtapp.setting

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentEditProfileBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.ProfileUpdateModelP
import com.ident.main.mngtapp.model.UserDataM
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.utils.SharedPrefClass
import com.ident.main.mngtapp.utils.UtilsFunctions
import retrofit2.Response


class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            updateUI()
            loginBTN.setOnClickListener {

                //   if (isValidate()) {
                if ((requireActivity() as SettingActivity).isOnline(requireContext())) {
                    val doctorId = SharedPrefClass().getPrefValue(
                        requireContext(), GlobalAppConstant.USERID
                    ).toString()
                    callAPI(
                        ProfileUpdateModelP(
                            12,
                            "Test", "0000000000", "02/01/1994", "Male", "Noida",
                            "Noida", "UP", "243201", "Status", "MBBS", "Specialist",
                            "ENT"
                        )
                    )
                } else {
                    showToast(requireActivity().resources.getString(R.string.please_connect_with_internet))
                }

                //  }
            }


        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        val model: String =
            SharedPrefClass().getPrefValue(requireContext(), GlobalAppConstant.USER_DATA).toString()
        val data: UserDataM = Gson().fromJson(model, UserDataM::class.java)

        viewDataBinding?.apply {
            if (data != null) {
                setImageWithUrl(data.profile_pics, imgPic)
                edtName.setText("" + data.name)
                edtEmail.setText("" + data.email)
                edtMobile.setText("" + data.phone)
                edtGender.setText("" + data.gender)
            }

        }

    }


    private fun callAPI(body: ProfileUpdateModelP) {
        (requireActivity() as SettingActivity).showLoader()
        ApiCall.getUpdateProfileAPI(body,
            object : ResponseListener<GenericModel> {
                override fun onSuccess(mResponse: Response<GenericModel>) {
                    (requireActivity() as SettingActivity).hideLoader()
                    if (mResponse.body()!!.success) {
                        viewDataBinding?.apply {
                            val data = mResponse.body()!!.data
                            val userData = Gson().toJson(data)
                            SharedPrefClass().putObject(
                                requireContext(),
                                GlobalAppConstant.USER_DATA,
                                userData
                            )
                            (activity as SettingActivity).supportFragmentManager.popBackStack()

                        }
                    } else {
                        showToast(resources.getString(R.string.data_not_found))
                    }
                }

                override fun onError(msg: String) {
                    (requireActivity() as SettingActivity).hideLoader()
                    showToast(getString(R.string.data_not_found))
                }

                override fun onError(msg: Boolean) {
                    (requireActivity() as SettingActivity).hideLoader()
                    showToast(getString(R.string.something_went_wrong))
                }
            })

    }

    private fun isValidate(): Boolean {
        viewDataBinding?.apply {
            return if (edtName.text.isNullOrEmpty()) {
                showToast(getString(R.string.valid_enter_email))
                false
            } else if (edtMobile.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_password))
                false
            } else if (!UtilsFunctions.isValidEmail(edtEmail.text.toString())) {
                showToast(getString(R.string.valid_email))
                false
            } else if (edtMobile.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_password))
                false
            }
//            else if (!UtilsFunctions.isValidEmail(btnRstPwd.text.toString())) {
//                showToast(getString(R.string.valid_email))
//                false
//            }
            else {
                true
            }
        }
        return true
    }


}