package com.ident.main.mngtapp.prescription

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.ArraySpinnerAdapter
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentAddPrescriptionBinding
import com.ident.main.mngtapp.databinding.SpinnerItemBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PrescriptionModelP
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import retrofit2.Response

class AddPrescriptionFragment :
    BaseFragment<FragmentAddPrescriptionBinding>(FragmentAddPrescriptionBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {


            btnDone.setOnClickListener {
                (activity as PrescriptionContainerActivity).supportFragmentManager.popBackStack()

            }
            /*
                        tvSavePrescreptions.setOnClickListener {

                            if (isValidate()) {
                                if (isOnline(this@AddPatientActivity)) {
                                    val doctorId = SharedPrefClass().getPrefValue(
                                        this@AddPatientActivity, GlobalAppConstant.USERID
                                    ).toString()

                                    callAPI(
                                        PrescriptionModelP(
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
            */

        }

        setMedicine()
        setDurationDays()
        setMorning()
        setNoon()
        setEvening()

    }

    private fun setDurationDays() {

        val days = ArrayList<String>()
        days.add("3 Days")
        days.add("5 Days")
        days.add("10 Days")
        days.add("15 Days")
        days.add("20 Days")

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            days,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, String> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: String
                ) {

                    binder?.textSpinner?.text = model

                }

            }
        )

        viewDataBinding?.apply {

            edtDays.setOnClickListener {
                mSPDays.performClick()
            }
            mSPDays.adapter = arrayAdapter
            mSPDays.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtDays.setText(days[p2])
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setMedicine() {

        val medicine = ArrayList<String>()
        medicine.add("Capsule 250 mg")
        medicine.add("Tab 250 mg")
        medicine.add("Drop 250 mg")
        medicine.add("Capsule 250 mg")
        medicine.add("Capsule 250 mg")

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            medicine,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, String> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: String
                ) {

                    binder?.textSpinner?.text = model

                }

            }
        )

        viewDataBinding?.apply {

            edtMedicine.setOnClickListener {
                mSpinnerMedicine.performClick()
            }
            mSpinnerMedicine.adapter = arrayAdapter
            mSpinnerMedicine.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtMedicine.setText(medicine[p2])
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setMorning() {

        val morning = ArrayList<String>()
        morning.add("0")
        morning.add("1")
        morning.add("2")

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            morning,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, String> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: String
                ) {

                    binder?.textSpinner?.text = model

                }

            }
        )

        viewDataBinding?.apply {

            edtDayMRN.setOnClickListener {
                mSPDayMRN.performClick()
            }
            mSPDayMRN.adapter = arrayAdapter
            mSPDayMRN.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtDayMRN.setText(morning[p2])
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setNoon() {

        val noon = ArrayList<String>()
        noon.add("0")
        noon.add("1")
        noon.add("2")

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            noon,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, String> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: String
                ) {

                    binder?.textSpinner?.text = model

                }

            }
        )

        viewDataBinding?.apply {

            edtNoon.setOnClickListener {
                mSPDayNOON.performClick()
            }
            mSPDayNOON.adapter = arrayAdapter
            mSPDayNOON.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtNoon.setText(noon[p2])
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setEvening() {

        val evening = ArrayList<String>()
        evening.add("0")
        evening.add("1")
        evening.add("2")

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            evening,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, String> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: String
                ) {

                    binder?.textSpinner?.text = model

                }

            }
        )

        viewDataBinding?.apply {

            edtEVN.setOnClickListener {
                mSPDayEVN.performClick()
            }
            mSPDayEVN.adapter = arrayAdapter
            mSPDayEVN.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtEVN.setText(evening[p2])
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun callAPI(body: PrescriptionModelP) {
        (requireActivity() as PrescriptionContainerActivity).showLoader()
        ApiCall.getAddPPrescriptionAPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as PrescriptionContainerActivity).hideLoader()

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
                (requireActivity() as PrescriptionContainerActivity).hideLoader()
                showToast(msg)
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as PrescriptionContainerActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

}