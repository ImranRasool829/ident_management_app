package com.ident.main.mngtapp.adapter

import android.view.View
import androidx.viewbinding.ViewBinding

interface RecyclerCallback<VM : ViewBinding, T> {
    fun bindData(binder: VM, model: T, position: Int, itemView: View)
    fun getFilterChar(row: T, s: String): Boolean{
        return false
    }
}
