package com.ident.main.mngtapp.billing

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.ArraySpinnerAdapter
import com.ident.main.mngtapp.adapter.RecyclerCallback
import com.ident.main.mngtapp.adapter.RecyclerViewGenricAdapter
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentBillingHistoryBinding
import com.ident.main.mngtapp.databinding.ItemAppointmentListLayoutBinding
import com.ident.main.mngtapp.databinding.ItemBillingServiceLayoutBinding
import com.ident.main.mngtapp.databinding.SpinnerItemBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.setting.SettingActivity
import retrofit2.Response

class BillingHistoryFragment : BaseFragment<FragmentBillingHistoryBinding>(
    FragmentBillingHistoryBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callAPI()
    }

    private fun callAPI() {
        (requireActivity() as SettingActivity).showLoader()
        ApiCall.getServiceAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as SettingActivity).hideLoader()
                if (mResponse.body()!!.success) {
                    viewDataBinding?.apply {
                        val list = mResponse.body()!!.data
                        if (list.size > 0) {
                            setAdapter(mRVBHistory, list)
                        } else {
                            showToast(getString(R.string.data_not_found))
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as SettingActivity).hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as SettingActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

    private fun setAdapter(
        recyclerView: RecyclerView, list: ArrayList<PatientDataModel>
    ) {
        val rvAdapProgress =
            RecyclerViewGenricAdapter<PatientDataModel,ItemBillingServiceLayoutBinding>(
                list,
                R.layout.item_billing_service_layout,
                object : RecyclerCallback<ItemBillingServiceLayoutBinding, PatientDataModel> {
                    override fun bindData(
                        binder: ItemBillingServiceLayoutBinding,
                        model: PatientDataModel,
                        position: Int,
                        itemView: View
                    ) {

                        binder.apply {
                            tvServiceName.text = model.name
                            tvAmount.text = model.price
                        }
                    }
                })
        recyclerView.adapter = rvAdapProgress

    }

}