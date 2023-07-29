package com.ident.main.mngtapp.base

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.SplashActivity
import com.ident.main.mngtapp.databinding.DialogCancalAppointmntBinding
import com.ident.main.mngtapp.databinding.DialogDelPresBinding
import com.ident.main.mngtapp.databinding.DialogLogoutBinding
import com.ident.main.mngtapp.model.ParamModel

class CustomDialog(val mContainer: SplashActivity) {
    //HomeActivity
    var dialog: Dialog? = null

    private fun initDialog(layout: View): Dialog {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
        dialog = CustomDialog(mContainer, R.style.mytheme)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(layout)
        return dialog!!
    }

    class CustomDialog(context: Context, themeId: Int) : Dialog(context, themeId) {
        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            currentFocus?.let {
                val imm: InputMethodManager = context.applicationContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as (InputMethodManager)
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
    }


    fun openCancelAppDialog(
        action: DialogListener
    ) {
        val binding = DataBindingUtil
            .inflate<DialogCancalAppointmntBinding>(
                LayoutInflater.from(mContainer),
                R.layout.dialog_cancal_appointmnt,
                null,
                false
            )

        val dialog = initDialog(binding.root)
        dialog.setCancelable(false)
        dialog.show()

        binding.apply {
            yesBT.setOnClickListener {
                action.positiveBtn()
                dialog.dismiss()
            }

            noBT.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun openDelPresDialog(
        action: DialogListener
    ) {
        val binding = DataBindingUtil
            .inflate<DialogDelPresBinding>(
                LayoutInflater.from(mContainer),
                R.layout.dialog_del_pres,
                null,
                false
            )

        val dialog = initDialog(binding.root)
        dialog.setCancelable(false)
        dialog.show()

        binding.apply {
            yesBT.setOnClickListener {
                action.positiveBtn()
                dialog.dismiss()
            }

            noBT.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun openLogoutDialog(
        action: DialogListener
    ) {
        val binding = DataBindingUtil
            .inflate<DialogLogoutBinding>(
                LayoutInflater.from(mContainer),
                R.layout.dialog_logout,
                null,
                false
            )

        val dialog = initDialog(binding.root)
        dialog.setCancelable(false)
        dialog.show()

        binding.apply {
            yesBT.setOnClickListener {
                action.positiveBtn()
                dialog.dismiss()
            }

            noBT.setOnClickListener {
                dialog.dismiss()
            }
        }
    }


    interface DialogListener {
        fun positiveBtn()
        fun positiveBtn(paramModel: ParamModel)
    }


}