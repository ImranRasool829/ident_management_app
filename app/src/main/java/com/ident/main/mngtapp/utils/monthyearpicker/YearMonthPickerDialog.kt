package com.ident.main.mngtapp.utils.monthyearpicker

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ident.main.mngtapp.R
import java.text.SimpleDateFormat
import java.util.*



/**
 * Created by Indu Bala on 06/12/21.
 */



/**
 * Creates a dialog for picking the year and month.
 */
class YearMonthPickerDialog(
    /**
     * Application's context.
     */
    private val mContext: Context,
    /**
     * Listener for user's date picking.
     */
    private var mOnDateSetListener: OnDateSetListener?, theme: Int,
    titleTextColor: Int, calendar: Calendar
) : DialogInterface.OnClickListener {
    /**
     * Set Init Date.
     */
    private val calendar: Calendar

    /**
     * The builder for our dialog.
     */
    private var mDialogBuilder: AlertDialog.Builder? = null

    /**
     * Resulting dialog.
     */
    private var mDialog: AlertDialog? = null

    /**
     * Custom user's theme for dialog.
     */
    private val mTheme: Int

    /**
     * Custom user's color for title text.
     */
    private val mTextTitleColor: Int

    /**
     * Picked year.
     */
    private var mYear = 0

    /**
     * Picked month.
     */
    private var mMonth = 0

    /**
     * Allow user to set custom date
     */
    private var mYearPicker: NumberPickerWithColor? = null
    private var mYearValue: TextView? = null

    /**
     * Creates a new YearMonthPickerDialog object that represents the dialog for
     * picking year and month.
     *
     * @param context           The application's context.
     * @param onDateSetListener Listener for user's date picking.
     */
    constructor(context: Context, calendar: Calendar, onDateSetListener: OnDateSetListener?) : this(
        context,
        onDateSetListener,
        -1,
        -1,
        calendar
    ) {
    }

    constructor(context: Context, onDateSetListener: OnDateSetListener?, calendar: Calendar) : this(
        context,
        onDateSetListener,
        -1,
        -1,
        calendar
    ) {
    }

    /**
     * Creates a new YearMonthPickerDialog object that represents the dialog for
     * picking year and month. Specifies custom user's theme
     *
     * @param context           The application's context.
     * @param onDateSetListener Listener for user's date picking.
     * @param theme             Custom user's theme for dialog.
     */
    constructor(
        context: Context,
        onDateSetListener: OnDateSetListener?,
        calendar: Calendar,
        theme: Int
    ) : this(context, onDateSetListener, theme, -1, calendar) {
    }

    /**
     * Listens for user's actions.
     *
     * @param dialog Current instance of dialog.
     * @param which  Id of pressed button.
     */
    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->                 //Check if user gave us a listener
                mOnDateSetListener?.onYearMonthSet(mYear, mMonth)
            DialogInterface.BUTTON_NEGATIVE ->                 //Exit the dialog
                dialog.cancel()
        }
    }

    /**
     * Creates and customizes a dialog.
     */
    private fun build() {
        //Applying user's theme
        var currentTheme = mTheme
        //If there is no custom theme, using default.
        if (currentTheme == -1) currentTheme = R.style.MyDialogTheme

        //Initializing dialog builder.
        mDialogBuilder = AlertDialog.Builder(mContext, currentTheme)

        //Creating View inflater.
        val layoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //Inflating custom title view.
        val titleView: View = layoutInflater.inflate(R.layout.view_dialog_title, null, false)
        //Inflating custom content view.
        val contentView: View = layoutInflater.inflate(R.layout.view_month_year_picker, null, false)

        //Initializing year and month pickers.
        mYearPicker = contentView.findViewById<View>(R.id.year_picker) as NumberPickerWithColor
        val monthPicker = contentView.findViewById<View>(R.id.month_picker) as NumberPickerWithColor

        //Initializing title text views
        val monthName = titleView.findViewById<View>(R.id.month_name) as TextView
        mYearValue = titleView.findViewById<View>(R.id.year_name) as TextView

        //If there is user's title color,
        if (mTextTitleColor != -1) {
            //Then apply it.
            setTextColor(monthName)
            setTextColor(mYearValue)
        }

        //Setting custom title view and content to dialog.
        mDialogBuilder!!.setCustomTitle(titleView)
        mDialogBuilder!!.setView(contentView)

        //Setting year's picker min and max value
        mYearPicker!!.minValue = MIN_YEAR
        mYearPicker!!.maxValue = MAX_YEAR

        //Setting month's picker min and max value
        monthPicker.minValue = 0
        monthPicker.maxValue = monthsList()!!.size - 1

        //Setting month list.
        monthPicker.displayedValues = monthsList()

        //Applying current date.
        setCurrentDate(mYearPicker, monthPicker, monthName, mYearValue)

        //Setting all listeners.
        setListeners(mYearPicker, monthPicker, monthName, mYearValue)

        //Setting titles and listeners for dialog buttons.
        mDialogBuilder!!.setPositiveButton("OK", this)
        mDialogBuilder!!.setNegativeButton("CANCEL", this)

        //Creating dialog.
        mDialog = mDialogBuilder!!.create()
    }

    /**
     * Sets color to given TextView.
     *
     * @param titleView Given TextView.
     */
    private fun setTextColor(titleView: TextView?) {
        titleView!!.setTextColor(ContextCompat.getColor(mContext, mTextTitleColor))
    }

    /**
     * Sets current date for title and pickers.
     *
     * @param yearPicker  year picker.
     * @param monthPicker month picker.
     * @param monthName   month name in the dialog title.
     * @param yearValue   year value in the dialog title.
     */
    private fun setCurrentDate(
        yearPicker: NumberPickerWithColor?,
        monthPicker: NumberPickerWithColor,
        monthName: TextView,
        yearValue: TextView?
    ) {
        //Getting current date values from Calendar instance.
        ////Calendar calendar = Calendar.getInstance();
        mMonth = calendar[Calendar.MONTH]
        mYear = calendar[Calendar.YEAR]

        //Setting output format.
        val monthFormat = SimpleDateFormat(MONTH_FORMAT, mCurrentLocale)

        //Setting current date values to dialog title views.
        monthName.text = monthFormat.format(calendar.time)
        yearValue!!.text = Integer.toString(mYear)

        //Setting current date values to pickers.
        monthPicker.value = mMonth
        yearPicker!!.value = mYear
    }

    /**
     * Sets current date for title and pickers.
     *
     * @param yearPicker  year picker.
     * @param monthPicker month picker.
     * @param monthName   month name in the dialog title.
     * @param yearValue   year value in the dialog title.
     */
    private fun setListeners(
        yearPicker: NumberPickerWithColor?,
        monthPicker: NumberPickerWithColor,
        monthName: TextView,
        yearValue: TextView?
    ) {
        //Setting listener to month name view.
        monthName.setOnClickListener {
            //If there's no month picker visible
            if (monthPicker.visibility == View.GONE) {
                //Set it visible
                monthPicker.visibility = View.VISIBLE

                //And hide year picker.
                yearPicker!!.visibility = View.GONE

                //Change title views alpha to picking effect.
                yearValue!!.alpha = 0.39f
                monthName.alpha = 1f
            }
        }

        //Setting listener to year value view.
        yearValue!!.setOnClickListener {
            //If there's no year picker visible
            if (yearPicker!!.visibility == View.GONE) {
                //Set it visible
                yearPicker.visibility = View.VISIBLE

                //And hide year picker.
                monthPicker.visibility = View.GONE

                //Change title views alpha to picking effect.
                monthName.alpha = 0.39f
                yearValue.alpha = 1f
            }
        }

        //Setting listener to month picker. So it can change title text value.
        monthPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            mMonth = newVal

            //Set title month text to picked month.
            monthName.text = monthsList()!![newVal]
        }

        //Setting listener to year picker. So it can change title text value.
        yearPicker!!.setOnValueChangedListener { picker, oldVal, newVal ->
            mYear = newVal

            //Set title year text to picked year.
            yearValue.text = Integer.toString(newVal)
        }
    }

    /**
     * Allows user to show created dialog.
     */
    fun show() {
        mDialog!!.show()
    }

    /**
     * Sets min value of year picker widget.
     * @param minYear The min value inclusive.
     */
    fun setMinYear(minYear: Int) {
        if (mYearPicker != null) {
            if (mYearPicker!!.value < minYear) {
                mYearPicker!!.value = minYear
                mYearValue!!.text = Integer.toString(minYear)
            }
            mYearPicker!!.minValue = Math.min(minYear, mYearPicker!!.maxValue)
        }
    }

    /**
     * Sets max value of year picker widget.
     * @param maxYear The max value inclusive.
     */
    fun setMaxYear(maxYear: Int) {
        if (mYearPicker != null) {
            if (mYearPicker!!.value > maxYear) {
                mYearPicker!!.value = maxYear
                mYearValue!!.text = Integer.toString(maxYear)
            }
            mYearPicker!!.maxValue = Math.max(maxYear, mYearPicker!!.minValue)
        }
    }

    /**
     * Interface for implementing user's pick listener.
     */
    interface OnDateSetListener {
        /**
         * Listens for user's actions.
         */
        fun onYearMonthSet(year: Int, month: Int)
    }

    companion object {
        /**
         * The minimal year value.
         */
        private const val MIN_YEAR = 1970

        /**
         * The maximum year value.
         */
        private const val MAX_YEAR = 2099

        /**
         * The Month format pattern.
         */
        private const val MONTH_FORMAT = "MMMM"

        /**
         * Array of months.
         */
        private var MONTHS_LIST: Array<String?>? = null

        /**
         * Specific locale for format datetime.
         */
        private var mCurrentLocale = Locale.getDefault()

        /**
         * Capitalize string
         */
        private fun capitalize(line: String): String {
            return Character.toUpperCase(line[0]).toString() + line.substring(1)
        }

        /**
         * Get month name with specified locale
         */
        private fun monthsList(): Array<String?>? {
            if (MONTHS_LIST == null) {
                val months = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
                val stringMonths = arrayOfNulls<String>(months.size)
                for (i in months.indices) {
                    val calendar = Calendar.getInstance()
                    val monthDate = SimpleDateFormat(MONTH_FORMAT, mCurrentLocale)
                    calendar[Calendar.MONTH] = months[i]
                    val monthName = monthDate.format(calendar.time)
                    stringMonths[i] = capitalize(monthName)
                }
                MONTHS_LIST = stringMonths
            }
            return MONTHS_LIST
        }
    }

    /**
     * Creates a new YearMonthPickerDialog object that represents the dialog for
     * picking year and month. Specifies custom user's theme and title text color
     *
     * @param context           The application's context.
     * @param onDateSetListener Listener for user's date picking.
     * @param theme             Custom user's theme for dialog.
     * @param titleTextColor    Custom user's color for title text.
     */
    init {
        mOnDateSetListener = mOnDateSetListener
        mTheme = theme
        mTextTitleColor = titleTextColor
        this.calendar = calendar

        //Set current locale of system
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCurrentLocale = mContext.resources.configuration.locales[0]
        } else {
            mCurrentLocale = mContext.resources.configuration.locale
        }

        //Builds the dialog using listed parameters.
        build()
    }

}