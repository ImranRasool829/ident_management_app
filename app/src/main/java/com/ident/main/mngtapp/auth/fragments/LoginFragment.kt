package com.ident.main.mngtapp.auth.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.auth.AuthActivity
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.dashboard.DashBoardActivity
import com.ident.main.mngtapp.databinding.FragmentLoginBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.LoginModelP
import com.ident.main.mngtapp.model.UserDataM
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.utils.SharedPrefClass
import com.ident.main.mngtapp.utils.UtilsFunctions
import retrofit2.Response

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {


    var isPasswordVisibe = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewDataBinding?.apply {


            edtPassword.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_RIGHT = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= edtPassword.right - edtPassword.compoundDrawables[DRAWABLE_RIGHT].getBounds()
                            .width()
                    ) {
                        isPasswordVisibe = !isPasswordVisibe

                        if (isPasswordVisibe) {
                            edtPassword.transformationMethod =
                                HideReturnsTransformationMethod.getInstance();
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_icon_visibility,
                                0
                            );
                        } else {
                            edtPassword.transformationMethod =
                                PasswordTransformationMethod.getInstance();
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.baseline_visibility_off_24,
                                0
                            );
                        }

                        return@OnTouchListener true
                    }
                }
                false
            })

            mForgetPw.setOnClickListener {
                showCustomDialogBox()
            }
            loginBTN.setOnClickListener {

                if (isValidate()) {

                    if ((requireActivity() as AuthActivity).isOnline(requireContext())) {
                        callAPI(
                            LoginModelP(
                                edtEmailMobile.text.toString().trim(),
                                edtPassword.text.toString().trim()
                            )
                        )
                    } else {
                        showToast("Please Connect with Internet")
                    }

                }
            }

        }
    }

    private fun callAPI(body: LoginModelP) {
        (requireActivity() as AuthActivity).showLoader()
        ApiCall.loginAPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {

                if (mResponse.body()!!.success) {
                    val token = mResponse.body()!!.token;
                    SharedPrefClass().putObject(
                        requireContext(),
                        GlobalAppConstant.ACCESS_TOKEN, token
                    )
                    callAPI1()
                } else {
                    (requireActivity() as AuthActivity).hideLoader()
                    if (mResponse.body()?.message != null && mResponse.body()!!.message != "") {
                        showToast(mResponse.body()!!.message)
                    } else {
                        showToast(requireActivity().resources.getString(R.string.please_enter_correct_password))
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as AuthActivity).hideLoader()
                showToast(msg)
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as AuthActivity).hideLoader()
                showToast(requireActivity().resources.getString(R.string.something_went_wrong))
            }
        })
    }

    private fun callAPI1() {
        ApiCall.getUserAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {

                (requireActivity() as AuthActivity).hideLoader()
                if (mResponse.body()!!.success) {
                    val model: UserDataM = mResponse.body()!!.user
                    SharedPrefClass().putObject(
                        requireContext(),
                        GlobalAppConstant.USERID,
                        model.id
                    )
                    val userData = Gson().toJson(model)
                    SharedPrefClass().putObject(
                        requireContext(),
                        GlobalAppConstant.USER_DATA,
                        userData
                    )
                    SharedPrefClass().putObject(requireContext(), GlobalAppConstant.IS_LOGIN, true)

                    (activity as AuthActivity).startActivity(
                        Intent(requireActivity(), DashBoardActivity::class.java)
                    )
                    ((activity) as AuthActivity).finish()

                }
            }

            override fun onError(msg: String) {
                (requireActivity() as AuthActivity).hideLoader()
                showToast(msg)
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as AuthActivity).hideLoader()
                showToast(requireActivity().resources.getString(R.string.something_went_wrong))
            }

        })
    }


    private fun isValidate(): Boolean {
        viewDataBinding?.apply {
            return if (edtEmailMobile.text.isNullOrEmpty()) {
                showToast(getString(R.string.valid_enter_email))
                false
            } else if (!UtilsFunctions.isValidEmail(edtEmailMobile.text.toString())) {
                showToast(getString(R.string.valid_email))
                false
            } else if (edtPassword.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_password))
                false
            } else {
                true
            }
        }
        return true
    }

    private fun showCustomDialogBox() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_forgot_password, null)
        builder.setView(dialogLayout)
        val dialog = builder.create()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = 900 // width in pixels
        lp.height = 900 // height in pixels
        lp.gravity = Gravity.CENTER
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.setCancelable(true)

        val doneBTNRP = dialog.findViewById<Button>(R.id.doneBTNRP)
        val crossTV = dialog.findViewById<TextView>(R.id.crossTV)


        crossTV.setOnClickListener {
            dialog.dismiss()
        }

        doneBTNRP.setOnClickListener {
            dialog.dismiss()
        }
    }


}