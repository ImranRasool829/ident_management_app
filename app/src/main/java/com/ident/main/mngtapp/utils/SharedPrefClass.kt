package com.ident.main.mngtapp.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import java.lang.reflect.Modifier


class SharedPrefClass {
    private val gson: Gson

    init {
        val mBuilder = GsonBuilder()
        mBuilder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        gson = mBuilder.serializeNulls().create()
    }

    fun getString(mContext: Context, key: String, value: String): String {
        val mPreferences =
            mContext.getSharedPreferences(GlobalAppConstant.SHARED_PREF, Context.MODE_PRIVATE)
        return mPreferences.getString(key, value) ?: ""
    }

    fun getBoolean(mContext: Context, key: String, value: Boolean): Boolean {
        val mPreferences =
            mContext.getSharedPreferences(GlobalAppConstant.SHARED_PREF, Context.MODE_PRIVATE)
        return mPreferences.getBoolean(key, value) ?: false
    }

    fun getPrefValue(mContext: Context, mValueKey: String): Any? {
        val mPreferences =
            mContext.getSharedPreferences(GlobalAppConstant.SHARED_PREF, Context.MODE_PRIVATE)
        val mMap = mPreferences.all
        if (mMap.isNotEmpty())
            if (mMap.containsKey(mValueKey))
                return mMap[mValueKey]
        return null
    }

    fun <T> getObject(mContext: Context, mObjectKey: String, aClass: Class<T>): T? {
        val mPreferences =
            mContext.getSharedPreferences(GlobalAppConstant.SHARED_PREF, Context.MODE_PRIVATE)
        val mData = mPreferences.getString(mObjectKey, null)
        return if (mData == null) {
            null
        } else {
            try {
                gson.fromJson(mData, aClass)
            } catch (e: Exception) {
                throw IllegalArgumentException("Object storaged with key $mObjectKey is instanceof other class")
            }

        }
    }

    fun putObject(mContext: Context, mObjectKey: String, mObject: Any?) {
        val mPreferences =
            mContext.getSharedPreferences(GlobalAppConstant.SHARED_PREF, Context.MODE_PRIVATE)
        val mEditor = mPreferences.edit()
        if (mObject == null || mObjectKey == "") {
            throw IllegalArgumentException("object/key is empty or null")
        }
        if (mObject.javaClass == String::class.java) {
            mEditor.putString(mObjectKey, mObject.toString())

        } else {
            mEditor.putString(mObjectKey, gson.toJson(mObject))
        }
        mEditor.apply()
    }

    fun clearAll(mContext: Context) {
        val mPreferences =
            mContext.getSharedPreferences(GlobalAppConstant.SHARED_PREF, Context.MODE_PRIVATE)
        val editor = mPreferences.edit()
        editor.clear()
        editor.apply()
    }
}