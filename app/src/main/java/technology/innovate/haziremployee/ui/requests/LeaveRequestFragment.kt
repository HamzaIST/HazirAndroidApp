package technology.innovate.haziremployee.ui.requests

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.braver.tool.picker.BraverDocPathUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.FragmentLeaveRequestBinding
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.AppUtils
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LeaveRequestFragment: Fragment(), LeaveClickListener {
    private lateinit var leaveRequestsAdapter: LeaveRequestsAdapter
    private val dateFormat = "dd-MM-yyyy"
    private var attachmentFileTextView: TextView? = null
    private lateinit var leaveBottomSheetDialog: BottomSheetDialog
    private val viewModel by viewModels<RequestsViewModel>()
    private lateinit var viewBinding: FragmentLeaveRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLeaveRequestBinding.inflate(inflater, container, false)

        initialiseRecyclerAdapter()
        loadAllLeaveRequestsFromRemote()
        setUpObservers()
        return viewBinding.root
    }

    private fun initialiseRecyclerAdapter() {
        leaveRequestsAdapter = LeaveRequestsAdapter(requireContext(), this, Collections.emptyList())
        viewBinding.recyclerView.adapter = leaveRequestsAdapter
    }

     fun loadAllLeaveRequestsFromRemote(){
        viewModel.employeeRequests(GetRequests(20, 1))
        viewModel.employeeRequests.observe(viewLifecycleOwner, employeeRequestsObserver)
    }

    private fun setUpObservers(){
        viewModel.selectedFileLiveData?.observe(viewLifecycleOwner) {
            if (attachmentFileTextView != null) {
                val fileName = AppUtils.getFileNameFromPath(it)
                attachmentFileTextView?.text = fileName
            }
        }

        viewModel.statusMessage.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                requireContext().showToast(it)
            }
        }
    }

    override fun updateLeaveRequest(leaveRequestsItem: LeaveRequestsItem) {
        showUpdateDialog(leaveRequestsItem)
    }

    private fun showUpdateDialog(leaveRequestsItem: LeaveRequestsItem) {
        val view = layoutInflater.inflate(R.layout.leave_request, null)
        leaveBottomSheetDialog = BottomSheetDialog(requireContext())

        val leaveTypesSpinner = view.findViewById<DynamicWidthSpinner>(R.id.leaveTypesSpinner)
        val descriptionTextView = view.findViewById<EditText>(R.id.descriptionTextView)
        val startDateTextView = view.findViewById<TextView>(R.id.startDateTextView)
        val endDateTextView = view.findViewById<TextView>(R.id.endDateTextView)
        val leaveBalanceTextView = view.findViewById<TextView>(R.id.leaveBalanceTextView)
        val attachCardView = view.findViewById<MaterialCardView>(R.id.attachCardView)
        val remove = view.findViewById<ImageView>(R.id.remove)
        attachmentFileTextView = view.findViewById(R.id.attachmentFileTextView)
        val submitMaterialButton = view.findViewById<MaterialButton>(R.id.submitMaterialButton)



        descriptionTextView.setText(leaveRequestsItem.description)
        startDateTextView.text = leaveRequestsItem.fromDate
        endDateTextView.text = leaveRequestsItem.toDate
        startDateTextView.setOnClickListener {
            startDateTextView.hideKeyboard()
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                startDateTextView.text = sdf.format(datePicker.selection?.first)
                endDateTextView.text = sdf.format(datePicker.selection?.second)

                val startDate: Long? = datePicker.selection?.first
                val endDate: Long? = datePicker.selection?.second

                val msDiff = endDate!!.minus(startDate!!)
                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                val days = daysDiff + 1;

                submitMaterialButton.text = "Submit($days day)"
            }
        }

        endDateTextView.setOnClickListener {
            endDateTextView?.hideKeyboard()
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                startDateTextView.text = sdf.format(datePicker.selection?.first)
                endDateTextView.text = sdf.format(datePicker.selection?.second)

                val startDate: Long? = datePicker.selection?.first
                val endDate: Long? = datePicker.selection?.second

                val msDiff = endDate!!.minus(startDate!!)
                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                val days = daysDiff + 1;

                submitMaterialButton.text = "Submit($days day)"
            }
        }

        attachCardView.setOnClickListener {
            Log.e("attachcardview","attachcardview")
            checkPermissionAndOpenPicker()
        }

        remove.setOnClickListener {
            viewModel.clearAttachment()
        }

        submitMaterialButton.setOnClickListener {
            viewModel.updateLeave(
                UpdateLeave(
                    leaveRequestsItem.id,
                    viewModel.selectedLeaveType?.value?.id,
                    descriptionTextView.text.toString(),
                    startDateTextView.text.toString(),
                    endDateTextView.text.toString()
                )

            )
            viewModel.updateLeave.observe(viewLifecycleOwner, updateLeaveObserver)
        }

        loadAllLeaveTypes(leaveTypesSpinner, leaveBalanceTextView, leaveRequestsItem)

        leaveBottomSheetDialog.setContentView(view)
        leaveBottomSheetDialog.show()

    }


    private fun checkPermissionAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        } else {
            openAttachments()
        }
    }

    private fun checkPermission() {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionNeededAlert()
            } else {
                openAttachments()
            }
    }

    private fun showPermissionNeededAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.needPermissions))
        builder.setMessage(getString(R.string.somePermissionsAreRequiredToDoTheTask))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
                permissionResult.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
        }
        builder.setNeutralButton(getString(R.string.cancel), null)
        val dialog = builder.create()
        dialog.show()
    }

    private var permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                println("it value = ${it.value}")
                if (it.value) {
                    openAttachments()
                }
            }
        }

    private fun loadAllLeaveTypes(
        leaveTypesSpinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView,
        leaveRequestsItem: LeaveRequestsItem
    ) {
        viewModel.getLeaveTypes()
        viewModel.leaveTypes.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    validateLeaveTypesData(it.item, leaveTypesSpinner, leaveBalanceTextView, leaveRequestsItem)
                }
                is DataState.Error -> {
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun validateLeaveTypesData(
        response: LeaveTypes,
        leaveTypesSpinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView,
        leaveRequestsItem: LeaveRequestsItem
    ) {
        if (activity != null && isAdded) {
            if (response.status == technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.OK) {
                if (response.data != null && response.data.isNotEmpty()) {
                    leaveBalanceTextView.text =
                        response.data[0]?.shortCode + "Left:" + response.data[0]?.balanceLeaves + " of " + response.data[0]?.noOfLeaves

                    val spinnerAdapter =
                        LeaveTypesSpinnerAdapter(requireContext(), response.data)
                    leaveTypesSpinner.apply {
                        adapter = spinnerAdapter
                        onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    if (p2 != 0) {
                                        viewModel.selectedLeaveType?.value =
                                            spinnerAdapter.result[p2 - 1]
                                    } else {
                                        viewModel.selectedLeaveType?.value = null

                                    }
                                }
                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }
                            }
                    }

                    for (bean in spinnerAdapter.result) {
                        if (bean?.id == leaveRequestsItem.id) {
                            val selectedPosition: Int =
                                spinnerAdapter.getPosition(bean)
                            leaveTypesSpinner.setSelection(selectedPosition + 1)
                        }
                    }


                } else {
                    val equipmentsSpinnerAdapter =
                        LeaveTypesSpinnerAdapter(requireContext(), Collections.emptyList())
                    leaveTypesSpinner.apply {
                        adapter = equipmentsSpinnerAdapter
                    }
                }

            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }

    private var updateLeaveObserver: Observer<DataState<ApiResponse2>> =
        androidx.lifecycle.Observer<DataState<ApiResponse2>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateUpdateLeaveResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateUpdateLeaveResponse(response: ApiResponse2) {
        if (response.status == technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.OK) {
            requireContext().showToast(response.message)
            if (leaveBottomSheetDialog.isShowing) {
                leaveBottomSheetDialog.dismiss()
            }
            viewModel.employeeRequests(GetRequests(20, 1))
            viewModel.employeeRequests.observe(viewLifecycleOwner, employeeRequestsObserver)
        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

    private var employeeRequestsObserver: Observer<DataState<EmployeeRequests>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateEmployeeRequestsData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateEmployeeRequestsData(response: EmployeeRequests) {
        if (activity != null && isAdded) {
            if (response.status == technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.OK) {
                if (response.data?.leaveRequests != null && response.data.leaveRequests.isNotEmpty()) {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.show()
                    leaveRequestsAdapter.setData(response.data.leaveRequests)
                } else {
                    leaveRequestsAdapter.setData(Collections.emptyList())

                    viewBinding.errorLayout.errorText.text = "No Data Found"
                    viewBinding.errorLayout.root.show()
                    viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                }

            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }

    private fun openAttachments() {
        try {
            val addAttachment = Intent(Intent.ACTION_GET_CONTENT)
            addAttachment.type = "*/*"
            addAttachment.action = Intent.ACTION_GET_CONTENT
            addAttachment.action = Intent.ACTION_OPEN_DOCUMENT
            activityResultLauncherForDocs.launch(addAttachment)
        } catch (exception: Exception) {
            AppUtils.printLogConsole("openAttachments", "Exception-------->" + exception.message)
        }
    }

    private var activityResultLauncherForDocs =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                try {
                    val selectedDocUri = result.data!!.data
                    if (selectedDocUri.toString().contains("video") || selectedDocUri.toString()
                            .contains("mp4") || selectedDocUri.toString()
                            .contains("mkv") || selectedDocUri.toString().contains("mov")
                    ) {

                        setSourcePathView(selectedDocUri.toString())
                    } else {
                        var tempFileAbsolutePath = BraverDocPathUtils.Companion.getSourceDocPath(
                            requireContext(),
                            selectedDocUri!!
                        )
                        setSourcePathView(tempFileAbsolutePath!!)
                    }
                } catch (e: java.lang.Exception) {
                    AppUtils.printLogConsole(
                        "activityResultLauncherForDocs",
                        "Exception-------->" + e.message
                    )
                }
            }
        }

    private fun setSourcePathView(path: String) {
        viewModel.selectedFileLiveData?.value = path

    }

    override fun onDeleteClicked(id: Int) {
        CustomDialog(requireActivity()).showDecisionButtonDialog(
            "Do you want to delete the request?",
            "Yes",
            "No",
            true,
            object : CustomDialog.onUserActionCLickListener {
                override fun negativeButtonClicked() {

                }

                override fun positiveButtonClicked() {
                    viewModel.deleteLeaveRequest(DeleteReq(id))
                    viewModel.deleteLeave.observe(viewLifecycleOwner, deleteLeaveRequestObserver)

                }

            })
    }

    private var deleteLeaveRequestObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateDeleteRequestResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()

                            }
                        })
                }
            }
        }

    private fun validateDeleteRequestResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            requireContext().showToast(response.message)
            loadAllLeaveRequestsFromRemote()
        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

}