package com.ident.main.mngtapp.appointment.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.ArraySpinnerAdapter
import com.ident.main.mngtapp.appointment.AppointmentContainerActivity
import com.ident.main.mngtapp.auth.AuthActivity
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentAddAppointmentBinding
import com.ident.main.mngtapp.databinding.SpinnerItemBinding
import com.ident.main.mngtapp.model.AddAppointmentMParam
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.LoginModelP
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.model.PrescriptionModelP
import com.ident.main.mngtapp.model.UpdateAppointmentMParam
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.prescription.PrescriptionContainerActivity
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response


class AddAppointmentFragment(addEditType: String?, data: String?) :
    BaseFragment<FragmentAddAppointmentBinding>(
        FragmentAddAppointmentBinding::inflate
    ) {
    private val mAddEditType: String = addEditType!!
    private val mData: String = data!!
    private lateinit var doctorID: String
    private lateinit var modelData: PatientDataModel
    private val doctorList = ArrayList<PatientDataModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {


            if (mAddEditType == "EDIT") {
                modelData = Gson().fromJson(mData, PatientDataModel::class.java)
                updateUI()
            }

            if ((requireActivity() as AppointmentContainerActivity).isOnline(requireContext())) {
                callAPI()
                callAPI1()
            } else {
                showToast(getString(R.string.please_connect_with_internet))
            }

            edtStartTime.setOnClickListener {
                ((activity) as (AppointmentContainerActivity)).timePicker(edtStartTime)
            }

            edtEndTime.setOnClickListener {
                ((activity) as (AppointmentContainerActivity)).timePicker(edtEndTime)
            }

            edtSelectDate.setOnClickListener {
                datePicker(edtSelectDate, min = true, max = false)
            }

            doneBTN.setOnClickListener {
                if (isValidate(mAddEditType)) {
                    if (mAddEditType == "EDIT") {

                        val type = edtType.text.toString()
                        val date = edtSelectDate.text.toString()
                        val startTime = edtStartTime.text.toString()
                        val endTime = edtEndTime.text.toString()
                        val purpose = edtPurpose.text.toString()

                        if ((requireActivity() as AppointmentContainerActivity).isOnline(
                                requireContext()
                            )
                        ) {
                            callAPI3(
                                UpdateAppointmentMParam(
                                    modelData.id.toString(),
                                    doctorList[mSpinnerDoctor.selectedItemPosition].id.toString(),
                                    type,
                                    date,
                                    startTime,
                                    endTime,
                                    purpose
                                )
                            )
                        } else {
                            showToast(getString(R.string.please_connect_with_internet))
                        }

                    } else {
                        val name = edtPN.text.toString()
                        val phone = edtPTNNumber.text.toString();
                        //  val doctorName = edtDoctor.text.toString()
                        val type = edtType.text.toString()
                        val date = edtSelectDate.text.toString()
                        val startTime = edtStartTime.text.toString()
                        val endTime = edtEndTime.text.toString()
                        val purpose = edtPurpose.text.toString()

                        if ((requireActivity() as AppointmentContainerActivity).isOnline(
                                requireContext()
                            )
                        ) {
                            callAPI2(
                                AddAppointmentMParam(
                                    name,
                                    phone,
                                    doctorList[mSpinnerDoctor.selectedItemPosition].id.toString(),
                                    type,
                                    date,
                                    startTime,
                                    endTime,
                                    purpose
                                )
                            )
                        } else {
                            showToast(getString(R.string.please_connect_with_internet))
                        }
                    }
                }

            }

        }

    }

    private fun callAPI3(body: UpdateAppointmentMParam) {
        (requireActivity() as AppointmentContainerActivity).showLoader()
        ApiCall.getUpdateAppointmentPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()

                if (mResponse.body()!!.success) {
                    showToast(mResponse.body()?.message)
                    requireActivity().finish()
                } else {
                    if (mResponse.body()?.message != null && mResponse.body()!!.message != "") {
                        showToast(mResponse.body()!!.message)
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(msg)
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

    private fun callAPI() {
        (requireActivity() as AppointmentContainerActivity).showLoader()
        ApiCall.getDoctorListAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                if (mResponse.body()!!.success) {

                    viewDataBinding?.apply {
                        doctorList.clear()
                        // doctorList= mResponse.body()!!.data
                        doctorList.addAll(mResponse.body()!!.data)
                        if (doctorList.size > 0) {
                            setSpinner(doctorList)
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })

    }

    private fun callAPI2(body: AddAppointmentMParam) {
        (requireActivity() as AppointmentContainerActivity).showLoader()
        ApiCall.getAddAppointmentPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()

                if (mResponse.body()!!.success) {
                    showToast(mResponse.body()?.message)
                    requireActivity().finish()
                } else {
                    if (mResponse.body()?.message != null && mResponse.body()!!.message != "") {
                        showToast(mResponse.body()!!.message)
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(msg)
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

    private fun updateUI() {
        viewDataBinding?.apply {
            mCardViewPN.visibility = View.GONE
            mCardViewMobileNum.visibility = View.GONE
            if (modelData != null) {
                //  edtDoctor.setText(modelData.name)
                // edtType.setText(modelData.department_name)
                edtSelectDate.setText(modelData.date_of_appointment)
                edtStartTime.setText(modelData.from_time)
                edtEndTime.setText(modelData.to_time)
                edtPurpose.setText(modelData.notes)

            }
        }

    }

    private fun setSpinner(doctorList: ArrayList<PatientDataModel>) {
        var mPosition: Int = 0
        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            doctorList,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, PatientDataModel> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: PatientDataModel
                ) {
                    binder?.textSpinner?.text = model.name
                    mPosition = position

                }

            }
        )

        viewDataBinding?.apply {

            edtDoctor.setOnClickListener {
                mSpinnerDoctor.performClick()
            }
            mSpinnerDoctor.adapter = arrayAdapter
            mSpinnerDoctor.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtDoctor.setText(doctorList[p2].name)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun isValidate(isEdit: String): Boolean {
        viewDataBinding?.apply {
            if (isEdit == "EDIT") {
                if (edtDoctor.text.isNullOrEmpty()) {
                    showToast(getString(R.string.select_doctor))
                    return false
                } else if (edtType.text.isNullOrEmpty()) {
                    showToast(getString(R.string.select_department))
                    return false
                } else if (edtSelectDate.text.isNullOrEmpty()) {
                    showToast(getString(R.string.select_date))
                    return false
                } else if (edtStartTime.text.isNullOrEmpty()) {
                    showToast(getString(R.string.start_time))
                } else if (edtEndTime.text.isNullOrEmpty()) {
                    showToast(getString(R.string.end_time))
                } else if (edtPurpose.text.isNullOrEmpty()) {
                    showToast(getString(R.string.purpose_of_visit))
                } else {
                    return true
                }
            } else {
                if (edtPN.text.isNullOrEmpty()) {
                    showToast(getString(R.string.patient_name))
                    return false
                } else if (edtPTNNumber.text.isNullOrEmpty()) {
                    showToast(getString(R.string.enter_phone_number))
                    return false
                } else if (edtDoctor.text.isNullOrEmpty()) {
                    showToast(getString(R.string.select_doctor))
                    return false
                } else if (edtType.text.isNullOrEmpty()) {
                    showToast(getString(R.string.select_department))
                    return false
                } else if (edtSelectDate.text.isNullOrEmpty()) {
                    showToast(getString(R.string.select_date))
                    return false
                } else if (edtStartTime.text.isNullOrEmpty()) {
                    showToast(getString(R.string.start_time))
                    return false
                } else if (edtEndTime.text.isNullOrEmpty()) {
                    showToast(getString(R.string.end_time))
                    return false
                } else if (edtPurpose.text.isNullOrEmpty()) {
                    showToast(getString(R.string.purpose_of_visit))
                } else {
                    return true
                }
            }
        }
        return true
    }

    private fun callAPI1() {
        (requireActivity() as AppointmentContainerActivity).showLoader()
        ApiCall.getDepartmentAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                if (mResponse.body()!!.success) {

                    viewDataBinding?.apply {
                        val department = mResponse.body()!!.data
                        if (department.size > 0) {
                            setDepartment(department)
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as AppointmentContainerActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })

    }

    private fun setDepartment(department: ArrayList<PatientDataModel>) {

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
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

            edtType.setOnClickListener {
                mSpinnerType.performClick()
            }

            mSpinnerType.adapter = arrayAdapter
            mSpinnerType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtType.setText(department[p2].department_name)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }


}