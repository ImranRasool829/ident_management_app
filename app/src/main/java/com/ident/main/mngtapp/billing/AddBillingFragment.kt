package com.ident.main.mngtapp.billing

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.adapter.ArraySpinnerAdapter
import com.ident.main.mngtapp.base.BaseFragment
import com.ident.main.mngtapp.databinding.FragmentAddBillingBinding
import com.ident.main.mngtapp.databinding.SpinnerItemBinding
import com.ident.main.mngtapp.model.GenericModel
import com.ident.main.mngtapp.model.PatientDataModel
import com.ident.main.mngtapp.network.ApiCall
import com.ident.main.mngtapp.network.ResponseListener
import com.ident.main.mngtapp.utils.SharedPrefClass
import retrofit2.Response


class AddBillingFragment : BaseFragment<FragmentAddBillingBinding>(
    FragmentAddBillingBinding::inflate
) {

    private var patientName: String = ""
    var date: String = ""
    private var service: String = ""
    private var serviceDescription: String = ""
    private var teethNumber: String = ""
    private var price: String = ""
    private var quantity: String = ""
    private var amount: String = ""

    private lateinit var mDialogEdtService: EditText;
    private lateinit var mDialogSpinnerService: Spinner
    private lateinit var mDialogDescription: EditText
    private lateinit var mDialogTeethNumber: EditText
    private lateinit var mDialogPrice: EditText
    private lateinit var mDialogQuantity: EditText
    private lateinit var mDialogAmount: EditText


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {

            edtSelectDate.setOnClickListener {
                datePicker(edtSelectDate, min = false, max = false)
            }

            addItemBTN.setOnClickListener {

                if (isValidate()) {
                    val patient = edtPatient.text.toString()
                    val date = edtSelectDate.text.toString()

                    addServiceDialog(patient, date)
                }
            }

            createInvoiceBTN.setOnClickListener {

                (activity as BillingContainerActivity).replaceFragment(
                    BillingSummaryFragment(
                        patientName,
                        date,
                        service,
                        serviceDescription,
                        teethNumber,
                        price,
                        quantity,
                        amount
                    ), true,
                    resources.getString(R.string.billing_summary)
                )
            }

        }

        if ((requireActivity() as BillingContainerActivity).isOnline(requireContext())) {
            val doctorId = SharedPrefClass().getPrefValue(
                requireContext(), GlobalAppConstant.USERID
            ).toString()
            if (doctorId != null && doctorId.isNotEmpty()) {
                callAPI(doctorId.toInt())
            }
        } else {
            showToast("Please Connect with Internet")
        }

    }

    private fun addServiceDialog(patient: String, date: String) {

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_product_layout, null)
        builder.setView(dialogLayout)
        val dialog = builder.create()

        dialog.show()

        val doneBTN = dialog.findViewById<AppCompatButton>(R.id.doneBTN)
        val imgCross = dialog.findViewById<ImageView>(R.id.imgCross)
        mDialogEdtService = dialog.findViewById<EditText>(R.id.edtService)
        mDialogSpinnerService = dialog.findViewById<Spinner>(R.id.mSpinnerService)
        mDialogDescription = dialog.findViewById<EditText>(R.id.edtDescription)
        mDialogTeethNumber = dialog.findViewById<EditText>(R.id.edtTeethNumber)
        mDialogPrice = dialog.findViewById<EditText>(R.id.edtPrice)
        mDialogQuantity = dialog.findViewById<EditText>(R.id.edtQty)
        mDialogAmount = dialog.findViewById<EditText>(R.id.edtAmount)

        imgCross.setOnClickListener {
            dialog.dismiss()
        }

        doneBTN.setOnClickListener {

            val service = mDialogEdtService.text.toString()

        }

    }


    private fun callAPI(body: Int) {
        (requireActivity() as BillingContainerActivity).showLoader()
        ApiCall.getPatientAPI(body, object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as BillingContainerActivity).hideLoader()
                if (mResponse.body()!!.success) {

                    viewDataBinding?.apply {
                        val patientList = mResponse.body()!!.data;
                        if (patientList.size > 0) {
                            setPatient(patientList)
                        } else {
                            showToast(getString(R.string.data_not_found))
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as BillingContainerActivity).hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as BillingContainerActivity).hideLoader()
                showToast(resources.getString(R.string.something_went_wrong))
            }
        })

    }

    private fun callAPI1() {
        (requireActivity() as BillingContainerActivity).showLoader()
        ApiCall.getServiceAPI(object : ResponseListener<GenericModel> {
            override fun onSuccess(mResponse: Response<GenericModel>) {
                (requireActivity() as BillingContainerActivity).hideLoader()
                if (mResponse.body()!!.success) {
                    viewDataBinding?.apply {
                        val list = mResponse.body()!!.data
                        if (list.size > 0) {
                            setService(list)
                        } else {
                            showToast(getString(R.string.data_not_found))
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                (requireActivity() as BillingContainerActivity).hideLoader()
                showToast(getString(R.string.data_not_found))
            }

            override fun onError(msg: Boolean) {
                (requireActivity() as BillingContainerActivity).hideLoader()
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

    private fun setPatient(patientList: ArrayList<PatientDataModel>) {

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            patientList,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, PatientDataModel> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: PatientDataModel
                ) {

                    binder?.textSpinner?.text = model.name

                }

            }
        )

        viewDataBinding?.apply {

            edtPatient.setOnClickListener {
                mSpinnerPatient.performClick()
            }
            mSpinnerPatient.adapter = arrayAdapter
            mSpinnerPatient.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    edtPatient.setText(patientList[p2].name)
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setService(list: ArrayList<PatientDataModel>) {

        val arrayAdapter = ArraySpinnerAdapter(requireContext(),
            list,
            object : ArraySpinnerAdapter.CustomSpinnerData<SpinnerItemBinding, PatientDataModel> {
                override fun setDataSpinner(
                    binder: SpinnerItemBinding?,
                    position: Int,
                    model: PatientDataModel
                ) {

                    binder?.textSpinner?.text = model.name

                }

            }
        )

        viewDataBinding?.apply {

            mDialogEdtService.setOnClickListener {
                mDialogSpinnerService.performClick()
            }
            mDialogSpinnerService.adapter = arrayAdapter
            mDialogSpinnerService.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    mDialogEdtService.setText(list[p2].name)
                    //  mSpinnerDR
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }


    private fun isValidate(): Boolean {
        viewDataBinding?.apply {
            return if (edtSelectDate.text.isNullOrEmpty()) {
                showToast(getString(R.string.select_date))
                false
            } else if (edtPatient.text.isNullOrEmpty()) {
                showToast(getString(R.string.select_patient))
                false
            } else {
                true
            }
        }
        return true
    }


}