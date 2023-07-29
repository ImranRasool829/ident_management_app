package com.ident.main.mngtapp.base

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.databinding.TakePictureDialogBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


abstract class BaseFragment<T : ViewBinding>(val bindingFactory: (LayoutInflater) -> T) :
    Fragment()/*,
    FragmentView*/ {

    private var timedialog: Dialog? = null
    var baseActivity: BaseActivity<ViewBinding>? = null

    //  var homeActivity: HomeActivity? = null
    var viewDataBinding: T? = null
    var mRootView: View? = null
    val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
    val patternPassword = Pattern.compile(passwordPattern)
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("en"))
    val format3 = SimpleDateFormat("MMMM dd,yyyy", Locale("en"))
    val AUTOCOMPLETE_REQUEST_CODE = 102
    var lat = 28.627981
    var long = 77.3648567
    var lat1 = 28.57362741108722
    var long1 = 77.26122999097018
    var selectPosition = 0


    enum class BUNDLECONSTANTS {
        TITLE, WEBURL
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewDataBinding == null) {
            viewDataBinding = bindingFactory(layoutInflater)
            return requireNotNull(viewDataBinding).root
        }
        return requireNotNull(viewDataBinding).root
    }


    fun hideStatusBar() {
        // homeActivity?.hideStatausBar()
    }

    fun underlineText(textview: TextView) {
        textview.paintFlags = textview.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    fun stringToFloat(value: String?): Float {
        return value?.toFloat() ?: 0f
    }

    fun stringToInt(value: String?): Int {
        return value?.toInt() ?: 0
    }

    override fun onStart() {
        super.onStart()
        //   homeActivity?.setToolbarText(getToolBar() ?: ToolBarModel())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // homeActivity = context as HomeActivity
        baseActivity = context as BaseActivity<ViewBinding>
    }

    fun clrNotification() {
        val nMgr =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancelAll()
    }

    fun setHtmlText(text: String): Spanned? {
        return Html.fromHtml(text)
    }

    fun setImgTint(img: ImageView, color: Int) {
        ImageViewCompat.setImageTintList(
            img,
            ColorStateList.valueOf(setColor(color))
        )
    }


    fun setColor(colorId: Int): Int {
        return ContextCompat.getColor(requireContext(), colorId)
    }

    fun setDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(requireContext(), id)
    }

    fun popBack() {
        // homeActivity?.onBackPressed()
//        homeActivity?.supportFragmentManager?.popBackStack()
    }

    fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(requireActivity(), colorId)
    }

    fun getStringText(id: Int): String {
        return getString(id)
    }

    fun showToast(msg: String?) {
        baseActivity?.showToast(msg)
    }

    fun showToast(msg: Int) {
        baseActivity?.showToast(msg)
    }


    fun createShapeDrawable() {
        var r = 8
        val shape =
            ShapeDrawable(RoundRectShape(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f), null, null))
        shape.paint.color = Color.RED
        //   view.setBackground(shape);
    }

    fun disableEMO(editText: EditText) {
        val EMOJI_FILTER = InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end) {
                val type = Character.getType(source.get(index))
                if (type == Character.SURROGATE.toInt() || source == "☺") {
                    return@InputFilter ""
                }
            }
            null
        }
        editText.filters = arrayOf(EMOJI_FILTER)
    }

    fun disableEmojiLen(editText: EditText, len: Int) {
        val SPACE_FILTER = InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end) {
                val type = Character.getType(source[index])
                if (type == Character.SURROGATE.toInt() || source == "☺") {
                    return@InputFilter ""
                }
            }
            null
        }
        editText.filters = arrayOf(SPACE_FILTER, LengthFilter(len))
    }

    private fun openSettings(activity: Activity) {

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, 101)
    }

    fun displayItAddStack(mFragment: Fragment, bundle: Bundle? = null) {
        if (bundle != null) {
            mFragment.arguments = bundle
        }
        //  homeActivity?.displayItAddStack(mFragment)
    }

    fun displayItNoStack(mFragment: Fragment, bundle: Bundle? = null) {
        if (bundle != null) {
            mFragment.arguments = bundle
        }
        // homeActivity?.displayItNoStack(mFragment)
    }


    fun displayItAddStack(mFragment: Fragment, bundle: Bundle? = null, conatinerId: Int?) {
        if (bundle != null) {
            mFragment.arguments = bundle
        }
        //  currentFragment = mFragment
        val fragmentTransaction = childFragmentManager
            .beginTransaction()

        // if (isBack) {
        fragmentTransaction.addToBackStack(mFragment::class.java.canonicalName)
        //}

        fragmentTransaction.replace(
            conatinerId!!,
            mFragment,
            mFragment::class.java.canonicalName
        )
            .commitAllowingStateLoss()
    }

    fun getDynamicText(id: Int, dynamicText: String?): String {
        return String.format(
            getString(id), dynamicText
        )
    }


    fun strikeText(content2: String, start: Int, end: Int): SpannableString {
        val spannableString2 = SpannableString(content2)
        spannableString2.setSpan(StrikethroughSpan(), start, end, 0)
        return spannableString2
    }

    fun strikeText(content2: String): SpannableString {
        val content = String.format(
            "", content2
        )
        val spannableString2 = SpannableString(content)
        spannableString2.setSpan(StrikethroughSpan(), 0, content.length, 0)
        return spannableString2
    }


    fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor = requireActivity().contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun getCount(textItemCount: AppCompatTextView): Int {
        var count: Int =
            textItemCount.text.trim().toString().toInt() + 1
        return count
    }

    fun getCountMinus(textItemCount: AppCompatTextView): Int {
        var count: Int =
            textItemCount.text.trim().toString().toInt() - 1
        return if (count >= 0)
            count
        else 1
    }

/*

    fun isLogin(): Boolean {
        return homeActivity?.isLogin() ?: false
    }
*/

    fun changeUTCDateFormat(utcDate: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = dateFormat.parse(utcDate)
            val formatter =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //If you need time just put specific format for time like ‘HH:mm:ss’
            return formatter.format(date)
        } catch (e: Exception) {
            utcDate
        }
    }

    fun changeDateFormat(startDate: String?): String {
        try {
            // "2021-03-11 18:53:38"
            var date = startDate
            var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val newDate: Date = spf.parse(date)
            spf = SimpleDateFormat("dd-MM-yyyy hh:mm a")
            return spf.format(newDate)
        } catch (e: Exception) {
            return startDate!!
        }
    }


    fun datePicker(eText: EditText, min: Boolean = false, max: Boolean = false) {
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]
        // date picker dialog
        // date picker dialog
        val picker = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
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

/*
    fun timePicker(fetchTimeSlot: ScheduleActivityFragment) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

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

                    fetchTimeSlot.getTimeSlot(formattedTime)
                    //    eText.setText(formattedTime)
                }
            }

        val timePicker: TimePickerDialog = TimePickerDialog(
            // pass the Context
            requireContext(),
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
*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Ddd", "Ddsdssd")
    }


    public fun setImageWithUri(
        uri: Uri?,
        imgProfile: ImageView?
    ) {
        Glide.with(baseActivity!!)
            .load(uri)
            .placeholder(R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imgProfile!!)
    }

    public fun setImageWithUrl(
        uri: String?,
        imgProfile: ImageView?
    ) {
        Glide.with(baseActivity!!)
            .load(uri)
            .placeholder(R.drawable.ic_usericon)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(200, 200)
            .skipMemoryCache(true)
            .into(imgProfile!!)
    }

    public fun setImageWithUri(
        uri: File?,
        imgProfile: ImageView?
    ) {
        Glide.with(baseActivity!!)
            .load(uri)
            .placeholder(R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imgProfile!!)
    }

    /*fun isOnline(context: Context): Boolean {
        return homeActivity?.isOnline(context) ?: false
    }*/
}