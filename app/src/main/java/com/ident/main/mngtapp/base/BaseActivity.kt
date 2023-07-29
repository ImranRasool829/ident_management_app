package com.ident.main.mngtapp.base

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ident.main.mngtapp.BuildConfig
import com.ident.main.mngtapp.IDentApplication
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.auth.AuthActivity
import com.ident.main.mngtapp.utils.SharedPrefClass
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity<T : ViewBinding>(val bindingFactory: (LayoutInflater) -> T) :
    AppCompatActivity() {

    private var reqCode: Int = 0
    private var permCallback: PermCallback? = null
    var viewDataBinding: T? = null
    private var progressDialog: Dialog? = null


    fun getviewDataBinding(): T {
        return viewDataBinding!!
    }


    fun hideStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = bindingFactory(layoutInflater)

        setContentView(viewDataBinding?.root)
    }

    override fun onResume() {
        super.onResume()

    }


    public fun setImageWithUrl(
        uri: String?,
        imgProfile: ImageView?
    ) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imgProfile!!)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {

                // if (currentFragment !is ChatFragment)
                (Objects.requireNonNull(this.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken, 0
                )
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        fun showToast(msg: String?) {
            try {
                if (!msg.isNullOrEmpty()) {
                    Toast.makeText(IDentApplication.instance, msg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
            }
        }

    }


    fun showToast(msg: String?) {
        try {
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {

        }
    }

    fun showToast(msg: Int) {
        try {
            Toast.makeText(this, getString(msg), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
        }
    }

    /*
    * Method to start progress dialog*/
    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*
     * Method to stop progress dialog*/
    fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun shareAppLink() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "IDent Management")
            var shareMessage = "\nLet me recommend you to use this application\n\n"
            shareMessage =
                """${shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }

    fun logout() {
        SharedPrefClass().clearAll(this)
        startActivity(
            Intent(
                this,
                AuthActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finishAffinity()
    }

    interface PermCallback {
        fun permGranted(resultCode: Int)
        fun permDenied(resultCode: Int)
    }


    fun checkPermissions(
        perms: Array<String>,
        requestCode: Int,
        permCallback: PermCallback
    ): Boolean {
        this.permCallback = permCallback
        this.reqCode = requestCode
        val permsArray = ArrayList<String>()
        var hasPerms = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permsArray.add(perm)
                hasPerms = false
            }
        }
        if (!hasPerms) {
            val permsString = arrayOfNulls<String>(permsArray.size)
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this@BaseActivity, permsString, 99)
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permGrantedBool = false
        when (requestCode) {
            99 -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showPermissionCustomDialog(
                            this,
                            getString(R.string.not_sufficient_permissions)
                        )
                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }

            9001 -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showPermissionCustomDialog(
                            this,
                            getString(R.string.not_sufficient_permissions)
                        )
                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }
        }
    }

    fun showPermissionCustomDialog(context: Context, message: String) {
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_permission_alert)
        val doneBT = dialog.findViewById<Button>(R.id.doneBT)
        val descTV = dialog.findViewById<TextView>(R.id.customAlertMsgTV)
        descTV.text = message
        doneBT.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)

        }
        dialog.show()
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    private var mDialogLoader: Dialog? = null

    fun showLoader() {
        hideLoader()
        mDialogLoader = setLoadingDialog(this);
    }

    fun hideLoader() {
        if (mDialogLoader != null && mDialogLoader!!.isShowing) {
            mDialogLoader?.cancel()
        }
    }

    fun currentDate(tvCurrentDate: TextView) {

        val sdf = SimpleDateFormat("d/M/yyyy")
        val currentDate = sdf.format(Date())
        tvCurrentDate.text = "$currentDate - "
    }

    fun timePicker(timePicker: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // logic to properly handle
                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }

                // fetchTimeSlot.getTimeSlot(formattedTime)
                timePicker.setText(formattedTime)
            }

        val timePicker: TimePickerDialog = TimePickerDialog(
            this@BaseActivity,
            // listener to perform task
            // when time is picked
            timePickerDialogListener,
            // default hour when the time picker
            // dialog is opened
            hour,
            // default minute when the time picker
            // dialog is opened
            minute,
            // 24 hours time picker is
            // false (varies according to the region)
            false
        )

        // then after building the timepicker
        // dialog show the dialog to user
        timePicker.show()

    }


    fun datePicker(eText: EditText, min: Boolean = false, max: Boolean = false) {
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]
        // date picker dialog
        val picker = DatePickerDialog(
            this@BaseActivity,
            { _, year, monthOfYear, dayOfMonth ->
                eText.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            },
            year,
            month,
            day
        )

        if (min)
            picker.datePicker.minDate = System.currentTimeMillis() - 1000
        if (max)
            picker.datePicker.maxDate = System.currentTimeMillis() - 1000


        picker.show()
    }

    fun deleteCustomDialog() {
        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_delete_patient, null)
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

        val doneBTNRP = dialog.findViewById<TextView>(R.id.noBT)
        val crossTV = dialog.findViewById<TextView>(R.id.yesBT)


        crossTV.setOnClickListener {
            dialog.dismiss()
        }

        doneBTNRP.setOnClickListener {
            dialog.dismiss()
        }
    }


    fun setLoadingDialog(activity: AppCompatActivity): Dialog {

        val dialog = Dialog(activity)
        dialog.show()
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.setContentView(R.layout.loader)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }


}