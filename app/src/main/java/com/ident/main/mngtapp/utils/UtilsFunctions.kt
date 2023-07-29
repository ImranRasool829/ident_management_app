package com.ident.main.mngtapp.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.IDentApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


object UtilsFunctions {

    @JvmStatic
    fun isPasswordValid(password: String): Boolean {
        val pattern: Pattern
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
        pattern = Pattern.compile(passwordRegex)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidEmail(data: String): Boolean {
        return data.matches(GlobalAppConstant.EMAIL_PATTERN.toRegex())
    }

    fun changeThemeMode(mode: Int) {
        if (mode == 0)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            IDentApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    @JvmStatic
    fun hideKeyBoard(view: View) {
        val imm = IDentApplication.instance
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun setError(editText: EditText?, errorMessage: String?) {
        editText!!.error = errorMessage
        editText.requestFocus()
    }

    fun createPartFromString(string: String): RequestBody {
        return string.toRequestBody(MultipartBody.FORM)
    }

    fun prepareFilePart(partName: String, fileUri: File): MultipartBody.Part {
        val requestFile = fileUri
            .asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, fileUri.name, requestFile)
    }

}