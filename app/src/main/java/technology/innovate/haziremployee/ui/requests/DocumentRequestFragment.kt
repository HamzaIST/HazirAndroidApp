package technology.innovate.haziremployee.ui.requests

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.braver.tool.picker.BraverDocPathUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.FragmentDocumentRequestBinding
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.AppUtils
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DocumentRequestFragment: Fragment(), DocumentClickListener {
    private lateinit var documentRequestAdapter: DocumentRequestAdapter
    private lateinit var documentRequestBottomSheetDialog: BottomSheetDialog
    private var calendar = Calendar.getInstance()
    private var attachmentFileTextView: TextView? = null
    private val dateFormat = "dd-MM-yyyy"
    private var downloadId: Long = 0
    private val viewModel by viewModels<RequestsViewModel>()
    private lateinit var viewBinding: FragmentDocumentRequestBinding
    private lateinit var otherRequestsItem: OtherRequestsItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDocumentRequestBinding.inflate(inflater, container, false)
//        initialiseRecyclerAdapter()
//        loadAllDocumentRequestsFromRemote()
//        setUpObservers()
        initialiseRecyclerAdapter()
        loadAllDocumentRequestsFromRemote()
        setUpObservers()
        return viewBinding.root
    }

    private fun initialiseRecyclerAdapter() {
        documentRequestAdapter =
            DocumentRequestAdapter(requireContext(), this, Collections.emptyList())
        viewBinding.recyclerView.adapter = documentRequestAdapter
    }

    fun loadAllDocumentRequestsFromRemote() {
        viewModel.employeeRequests(GetRequests(20, 1))
        viewModel.employeeRequests.observe(viewLifecycleOwner, employeeRequestsObserver)
    }

    private fun setUpObservers() {
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
                if (response.data?.otherRequests != null && response.data.otherRequests.isNotEmpty()) {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.show()
                    documentRequestAdapter.setData(response.data.otherRequests)
                } else {
                    documentRequestAdapter.setData(Collections.emptyList())

                    viewBinding.errorLayout.errorText.text = "No Data Found"
                    viewBinding.errorLayout.root.show()
                    viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                }

            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }


    override fun updateDocRequest(otherRequestsItem: OtherRequestsItem) {
        showUpdateDailog(otherRequestsItem)

    }

    private fun showUpdateDailog(otherRequestsItem: OtherRequestsItem) {
        val view = layoutInflater.inflate(R.layout.document_request, null)
        documentRequestBottomSheetDialog = BottomSheetDialog(requireContext())

        val documentTypesSpinner = view.findViewById<DynamicWidthSpinner>(R.id.documentTypesSpinner)
        val descriptionTextView = view.findViewById<EditText>(R.id.descriptionTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val emailAddressTextView = view.findViewById<EditText>(R.id.emailAddressTextView)
        val attachCardView = view.findViewById<MaterialCardView>(R.id.attachCardView)
        val remove = view.findViewById<ImageView>(R.id.remove)
        attachmentFileTextView = view.findViewById(R.id.attachmentFileTextView)
        val submitMaterialButton = view.findViewById<MaterialButton>(R.id.submitMaterialButton)


        dateTextView.text = otherRequestsItem.required_by
        descriptionTextView.setText(otherRequestsItem.description)
        SessionManager.init(requireContext())
        SessionManager.user?.let { emailAddressTextView.setText(it.username) }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateTextView.text = SimpleDateFormat(dateFormat, Locale.US).format(calendar.time)
            }
        val datePicker = DatePickerDialog(
            requireActivity(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = calendar.timeInMillis


        dateTextView.setOnClickListener {
            datePicker.show()
        }

        attachCardView.setOnClickListener {
           // checkPermissionAndOpenPicker()
        }

        remove.setOnClickListener {
            viewModel.clearAttachment()
        }


        submitMaterialButton.setOnClickListener {
            val request =
                UpdateDocumentRequest(
                    "Document Request",
                    descriptionTextView?.text?.toString()?.trim(),
                    "",
                    viewModel.selectedDocumentRequestType?.value?.id,
                    dateTextView.text.toString(),
                    otherRequestsItem.id
                )
            viewModel.updateDocument(request)
            viewModel.updateDocument.observe(viewLifecycleOwner, updateDocumentObserver)

        }

        loadAllDocumentRequestTypes(documentTypesSpinner, otherRequestsItem)

        documentRequestBottomSheetDialog.setContentView(view)
        documentRequestBottomSheetDialog.show()


    }


    private fun checkPermissionAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        } else {
            openAttachments()
        }
        openAttachments()
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

    private fun checkPermissionAndAllowFileWrite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkFileWritePermission()
        } else {
            downloadFile()
        }
    }

    private fun checkFileWritePermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showFileWritePermissionNeededAlert()
        } else {
            downloadFile()
        }
    }

    private fun showFileWritePermissionNeededAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.needPermissions))
        builder.setMessage(getString(R.string.somePermissionsAreRequiredToDoTheTask))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            fileWritePermissionResult.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
        builder.setNeutralButton(getString(R.string.cancel), null)
        val dialog = builder.create()
        dialog.show()
    }

    private var fileWritePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                println("it value = ${it.value}")
                if (it.value) {
                    downloadFile()
                }
            }
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


    private fun loadAllDocumentRequestTypes(
        spinner: DynamicWidthSpinner,
        otherRequestsItem: OtherRequestsItem
    ) {
        viewModel.getRequestTypes()
        viewModel.documentRequestTypes.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    validateDocumentRequestTypesData(it.item, spinner, otherRequestsItem)
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

    private fun validateDocumentRequestTypesData(
        response: RequestType,
        spinner: DynamicWidthSpinner,
        otherRequestsItem: OtherRequestsItem
    ) {
        if (activity != null && isAdded) {
            if (response.status == Constants.API_RESPONSE_CODE.OK) {
                if (response.requestTypeItem != null && response.requestTypeItem.isNotEmpty()) {
                    val spinnerAdapter =
                        DocumentRequestTypesSpinnerAdapter(
                            requireContext(),
                            response.requestTypeItem
                        )
                    spinner.apply {
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
                                        viewModel.selectedDocumentRequestType?.value =
                                            spinnerAdapter.result[p2 - 1]
                                    } else {
                                        viewModel.selectedDocumentRequestType?.value = null

                                    }
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }
                            }
                    }

                    for (bean in spinnerAdapter.result) {
                        if (bean?.id == otherRequestsItem.id) {
                            val selectedPosition: Int =
                                spinnerAdapter.getPosition(bean)
                            spinner.setSelection(selectedPosition + 1)
                        }
                    }

                } else {
                    val spinnerAdapter =
                        LeaveTypesSpinnerAdapter(requireContext(), Collections.emptyList())
                    spinner.apply {
                        adapter = spinnerAdapter
                    }
                }

            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }


    private var updateDocumentObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateUpdateDocumentResponse(it.item)
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

    private fun validateUpdateDocumentResponse(response: ApiResponse) {
        if (response.status == technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.OK) {
            requireContext().showToast(response.message)
            if (documentRequestBottomSheetDialog.isShowing) {
                documentRequestBottomSheetDialog.dismiss()
            }
            viewModel.employeeRequests(GetRequests(20, 1))
            viewModel.employeeRequests.observe(viewLifecycleOwner, employeeRequestsObserver)
        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
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

    override fun onDeleteClick(id: Int) {
        CustomDialog(requireActivity()).showDecisionButtonDialog(
            "Do you want to delete the request?",
            "Yes",
            "No",
            true,
            object : CustomDialog.onUserActionCLickListener {
                override fun negativeButtonClicked() {

                }

                override fun positiveButtonClicked() {
                    viewModel.deleteDocumentRequest(DeleteReq(id))
                    viewModel.deleteDocumentRequest.observe(
                        viewLifecycleOwner,
                        deleteDocumentRequestObserver
                    )

                }

            })

    }

    private var deleteDocumentRequestObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateDeleteDocumentRequestResponse(it.item)
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

    private fun validateDeleteDocumentRequestResponse(response: ApiResponse) {
        if (response.status == technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.OK) {
            requireContext().showToast(response.message)
            loadAllDocumentRequestsFromRemote()
        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

    override fun downloadDoc(otherRequestsItem: OtherRequestsItem) {
        this.otherRequestsItem = otherRequestsItem
        //checkPermissionAndAllowFileWrite()
           downloadFile()
        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            101)
    }
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
           // Toast.makeText(requireActivity(), "Permission already granted", Toast.LENGTH_SHORT).show()
            downloadFile()
        }
    }

    private fun downloadFile() {
        if (otherRequestsItem.doc_url_from_admin != null) {
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val nowTime = sdf.format(Date())
            val docLink = Uri.parse(otherRequestsItem.doc_url_from_admin)
            val request = DownloadManager.Request(docLink)
                .setTitle(nowTime)
                .setDescription("Download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
            val downloadManager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(request)

        } else {
            Toast.makeText(
                requireContext(),
                "No Attachment is Available for Download",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    var receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id: Long? = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                requireContext().showToast("Download Completed")
            }
        }

    }


}