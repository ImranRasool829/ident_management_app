package com.ident.main.mngtapp.utils.monthyearpicker

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.NumberPicker
import androidx.appcompat.content.res.AppCompatResources
import java.lang.reflect.Field


/**
 * Created by Indu Bala on 06/12/21.
 */
class NumberPickerWithColor(context: Context?, attrs: AttributeSet?) :
    NumberPicker(context, attrs) {
    init {
        var numberPickerClass: Class<*>? = null
        try {
            numberPickerClass = Class.forName("android.widget.NumberPicker")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        var selectionDivider: Field? = null
        try {
            selectionDivider = numberPickerClass!!.getDeclaredField("mSelectionDivider")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        try {
            selectionDivider?.isAccessible = true
            selectionDivider?.set(
                this,
                AppCompatResources.getDrawable(
                    context!!,
                    cn.jeesoft.widget.pickerview.R.drawable.j_bg_line_timepicker
                )
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}
