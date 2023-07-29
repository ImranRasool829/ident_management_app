package com.ident.main.mngtapp.patient

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.ArraySpinnerAdapter
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityAddPatientBinding
import com.ident.main.mngtapp.databinding.SpinnerItemBinding
import com.ident.main.mngtapp.model.AddPatientPD
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response

class AddPatientActivity :
    BaseActivity<ActivityAddPatientBinding>(ActivityAddPatientBinding::inflate) {

    private var mTobacco: String = "NO"
    private var mSmoking: String = "NO"
    private var mDrinking: String = "NO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding?.apply {
            toolBar.tvTitle.text = resources.getString(R.string.add_patient)

            toolBar.imgBack.setOnClickListener {
                finish()
            }

            if (isOnline(this@AddPatientActivity)) {
                callAPI()
            } else {
                showToast(resources.getString(R.string.please_connect_with_internet))
            }

            val radioButtonID: Int = rbGrp.checkedRadioButtonId
            val radioButton: View = rbGrp.findViewById(radioButtonID)
            val idx: Int = rbGrp.indexOfChild(radioButton)
            val r: RadioButton = rbGrp.getChildAt(idx) as RadioButton


            mSmokingCB.setOnCheckedChangeListener { _, isChecked ->
                mSmoking = if (isChecked) {
                    "YES"
                } else {
                    "NO"
                }
            }

            mDrinkingCB.setOnCheckedChangeListener { _, isChecked ->
                mDrinking = if (isChecked) {
                    "YES"
                } else {
                    "NO"
                }
            }


            mTobaccoCB.setOnCheckedChangeListener { _, isChecked ->
                mTobacco = if (isChecked) {
                    "YES"
                } else {
                    "NO"
                }
            }

            tvAddPatientBTN.setOnClickListener {
                if (isValidate()) {
                    if (isOnline(this@AddPatientActivity)) {
                        val doctorId = SharedPrefClass().getPrefValue(
                            this@AddPatientActivity, GlobalAppConstant.USERID
                        ).toString()

                        callAPI1(
                            AddPatientPD(
                                doctorId,
                                edtPN.text.toString(),
                                edtPhoneNum.text.toString(),
                                edtEmailP.text.toString(),
                                r.text.toString(),
                                edtDOB.text.toString(),
                                edtReasonTC.text.toString(),
                                edtSelectElergy.text.toString(),
                                mSmoking,
                                mDrinking,
                                mTobacco,
                                edtSelectDepartment.text.toString(),
                                edtAddress.text.toString()
                            )
                        )
                    } else {
                        showToast(resources.getString(R.string.please_connect_with_internet))
                    }
                }

            }

        }

    }

    private fun callAPI() {
        showLoader()
        ApiCall.getDepartmentAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                hideLoader()
                if (mResponse.body()!!.success) {

                    viewDataBinding?.apply {
                        val department = mResponse.body()!!.data
                        if (department.size > 0) {
                            setDepartment(department)
                        } else {
                            showToast(getString(R.string.data_not_found))
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })

    }

    private fun setDepartment(department: ArrayList<PatientDataModel>) {

        val arrayAdapter = ArraySpinnerAdapter(this@AddPatientActivity,
            department,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, PatientDataModel> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: PatientDataModel
                ) {

                    binder?.textSpinner?.text = model.department_name

                }

            }
        )

        viewDataBinding?.apply {

            edtSelectDepartment.setOnClickListener {
                mSpinnerDepartment.performClick()
            }

            mSpinnerDepartment.adapter = arrayAdapter
            mSpinnerDepartment.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtSelectDepartment.setText(department[p2].department_name)
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun callAPI1(body: AddPatientPD) {
        showLoader()
        ApiCall.getAddPatientDAPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                hideLoader()
                if (mResponse.body()!!.success) {
                    showToast(mResponse.body()?.message)
                    finish()

                } else {
                    hideLoader()
                    if (mResponse.body()?.message != null && mResponse.body()!!.message != "") {
                        showToast(mResponse.body()!!.message)
                    } else {
                        showToast(resources.getString(R.string.something_went_wrong))
                    }
                }
            }

            override fun onError(msg: String) {
                hideLoader()
                showToast(msg)
            }

            override fun onError(msg: Boolean) {
                hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

    private fun isValidate(): Boolean {
        viewDataBinding?.apply {
            if (edtPN.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_your_name))
                return false
            } else if (edtPhoneNum.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_phone_number))
                return false
            } else if (edtEmailP.text.isNullOrEmpty()) {
                showToast(getString(R.string.enter_email))
                return false
            } else if (edtDOB.text.isNullOrEmpty()) {
                showToast(getString(R.string.patient_age))
                return false
            } else if (edtReasonTC.text.isNullOrEmpty()) {
                showToast(getString(R.string.reason_to_come))
                return false
            } else if (edtSelectElergy.text.isNullOrEmpty()) {
                showToast(getString(R.string.allergy))
                return false
            } else if (edtAddress.text.isNullOrEmpty()) {
                showToast(getString(R.string.address))
                return false
            } else {
                return true
            }
        }
        return true
    }


}