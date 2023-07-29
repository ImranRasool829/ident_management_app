package com.ident.main.mngtapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ident.main.mngtapp.R


class ArraySpinnerAdapter<T, VM : ViewDataBinding>(
    val mContext: Context,
    val list: ArrayList<T>,
    val customSpinnerData: CustomSpinnerData<VM, T>
) :
    BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, p2: ViewGroup?): View {
        var view = view
        val inflter = LayoutInflater.from(mContext)
        view = inflter.inflate(R.layout.spinner_item, null)
        val viewDataBinding = DataBindingUtil.bind<VM>(view)
        customSpinnerData.setDataSpinner(viewDataBinding, i, list[i])
        return view
    }

    interface CustomSpinnerData<VM : ViewDataBinding, T> {
        fun setDataSpinner(binder: VM?, position: Int, t: T)
    }


}