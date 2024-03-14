//package technology.dubaileading.maccessemployee.ui.requests
//
//import android.app.Activity
//import android.app.DatePickerDialog
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//import android.widget.*
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.widget.AppCompatButton
//import androidx.appcompat.widget.AppCompatSpinner
//import androidx.cardview.widget.CardView
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.ViewModelProvider
//import androidx.viewpager2.widget.ViewPager2
//import com.braver.tool.picker.BraverDocPathUtils
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.material.datepicker.MaterialDatePicker
//import com.google.android.material.tabs.TabLayout
//import technology.dubaileading.maccessemployee.R
//import technology.dubaileading.maccessemployee.base.BaseFragment
//import technology.dubaileading.maccessemployee.databinding.FragmentRequestsBinding
//import technology.dubaileading.maccessemployee.rest.entity.*
//import technology.dubaileading.maccessemployee.utils.AppShared
//import technology.dubaileading.maccessemployee.utils.AppUtils
//import technology.dubaileading.maccessemployee.utils.PermissionUtils
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//
//class RequestsFragment : BaseFragment<FragmentRequestsBinding, RequestsViewModel>() {
//    private var cal = Calendar.getInstance()
//    private var leaveTypeList = ArrayList<LeaveTypeData>()
//    private var docTypeList = ArrayList<RequestTypeItem>()
//    private var fromDate: String = ""
//    private var toDate: String = ""
//    private var reqDate: String = ""
//    private var IS_FROM_DATE = false
//    private var REQUEST_CODE_PICK_DOC = 101
//    private var doc_type_id: Int? = null
//    private var attachmentFileTextView: TextView? = null
//    private var leaveAttachmentFileTextView: TextView? = null
//    private lateinit var newDocDialog : BottomSheetDialog
//    private lateinit var newLeaveDialog : BottomSheetDialog
//    private val dateFormat = "dd-MM-yyyy"
//
//
//    override fun createViewModel(): RequestsViewModel {
//        return ViewModelProvider(this).get(RequestsViewModel::class.java)
//    }
//
//    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentRequestsBinding {
//        return FragmentRequestsBinding.inflate(layoutInflater!!)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding?.newRequest?.setOnClickListener {
//            newRequest()
//        }
//
//    }
//
//    private fun newRequest() {
//        val btnsheet = layoutInflater.inflate(R.layout.new_request_layout, null)
//        var newRequestDialog = BottomSheetDialog(requireContext())
//        val requestLeave = btnsheet.findViewById<LinearLayout>(R.id.request_leave)
//        val requestDoc = btnsheet.findViewById<LinearLayout>(R.id.request_doc)
//
//        requestLeave.setOnClickListener {
//            newRequestDialog.dismiss()
//            newLeaveRequest()
//        }
//
//        requestDoc.setOnClickListener {
//            newRequestDialog.dismiss()
//            newDocRequest()
//        }
//        newRequestDialog.setContentView(btnsheet)
//        newRequestDialog.show()
//    }
//
//    private fun newDocRequest() {
//        val btnsheet = layoutInflater.inflate(R.layout.document_request, null)
//        newDocDialog = BottomSheetDialog(requireContext())
//
//        val spinnerDocType = btnsheet.findViewById<AppCompatSpinner>(R.id.spinnerDocType)
//        val dateCard = btnsheet.findViewById<CardView>(R.id.dateCard)
//        val date = btnsheet.findViewById<EditText>(R.id.date)
//        val email = btnsheet.findViewById<EditText>(R.id.email)
//        val attachCardView = btnsheet.findViewById<CardView>(R.id.attachCardView)
//        val desc = btnsheet.findViewById<EditText>(R.id.desc)
//        val remove = btnsheet.findViewById<ImageView>(R.id.remove)
//        attachmentFileTextView = btnsheet.findViewById<TextView>(R.id.attachmentFileTextView)
//
//        val submitBt = btnsheet.findViewById<AppCompatButton>(R.id.submitBt)
//
//        var docType = ArrayList<String>()
//        if (docTypeList.isNotEmpty()) {
//            for (i in docTypeList.indices) {
//                docType.add(docTypeList[i].type.toString())
//            }
//            val arrayAdapter =
//                ArrayAdapter(
//                    requireContext(),
//                    android.R.layout.simple_spinner_dropdown_item,
//                    docType
//                )
//            spinnerDocType.adapter = arrayAdapter
//            doc_type_id = docTypeList[0].id
//
//            spinnerDocType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    adapterView: AdapterView<*>,
//                    view: View,
//                    i: Int,
//                    l: Long
//                ) {
//                    val docType = docTypeList[i].type
//                    doc_type_id = docTypeList[i].id
//                    if (docType.equals("Others")) {
//
//                    }
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>?) {
//
//                }
//            }
//        }
//
//        val dateSetListener =
//            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
//                cal.set(Calendar.YEAR, year)
//                cal.set(Calendar.MONTH, monthOfYear)
//                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//
//                val sdf = SimpleDateFormat(dateFormat, Locale.US)
//                reqDate = sdf.format(cal.time)
//                date.setText(reqDate)
//            }
//        var datePicker = DatePickerDialog(
//            requireActivity(),
//            dateSetListener,
//            cal.get(Calendar.YEAR),
//            cal.get(Calendar.MONTH),
//            cal.get(Calendar.DAY_OF_MONTH)
//        )
//        datePicker.datePicker.minDate = cal.timeInMillis
//
//        date.setOnClickListener {
//            datePicker.show()
//        }
//
//        var user = AppShared(activity as Context).getUser()
//        email.setText(user.data?.username)
//
//        attachCardView.setOnClickListener {
//            callFileAccessIntent()
//        }
//
//        remove.setOnClickListener {
//            viewModel?.clearAttachment()
//        }
//
//
//        submitBt.setOnClickListener {
//            val description = desc?.text?.toString()?.trim()
//            val mail = email?.text?.toString()?.trim()
//            var documentRequest =
//                DocumentRequest("Document Request", description, "", doc_type_id, reqDate, mail)
//            viewModel?.documentRequest(requireContext(), documentRequest)
//        }
//
//
//
//        newDocDialog.setContentView(btnsheet)
//        newDocDialog.show()
//
//    }
//
//    /**
//     * Declaration callFileAccessIntent()
//     * Handling click event of the picker icons
//     */
//    private fun callFileAccessIntent() {
//        if (PermissionUtils.isFileAccessPermissionGranted(requireContext())) {
//            openAttachments()
//        } else {
//            PermissionUtils.showFileAccessPermissionRequestDialog(requireContext())
//        }
//    }
//
//
//    /**
//     * Handling Get doc,pdf,xlx,apk,et., from user to send throw Attachments <br></br>
//     */
//    private fun openAttachments() {
//        try {
//            val addAttachment = Intent(Intent.ACTION_GET_CONTENT)
//            addAttachment.type = "*/*"
//            addAttachment.action = Intent.ACTION_GET_CONTENT
//            addAttachment.action = Intent.ACTION_OPEN_DOCUMENT
//            activityResultLauncherForDocs.launch(addAttachment)
//        } catch (exception: Exception) {
//            AppUtils.printLogConsole("openAttachments", "Exception-------->" + exception.message)
//        }
//    }
//
//    var activityResultLauncherForDocs =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
//                try {
//                    //String tempFileAbsolutePath = "";
//                    val selectedDocUri = result.data!!.data
//                    if (selectedDocUri.toString().contains("video") || selectedDocUri.toString()
//                            .contains("mp4") || selectedDocUri.toString()
//                            .contains("mkv") || selectedDocUri.toString().contains("mov")
//                    ) {
//
//                        setSourcePathView(selectedDocUri.toString())
//                    } else {
//                        var tempFileAbsolutePath = BraverDocPathUtils.Companion.getSourceDocPath(
//                            requireContext(),
//                            selectedDocUri!!
//                        )
//                        setSourcePathView(tempFileAbsolutePath!!)
//                    }
//                } catch (e: java.lang.Exception) {
//                    AppUtils.printLogConsole(
//                        "activityResultLauncherForDocs",
//                        "Exception-------->" + e.message
//                    )
//                }
//            }
//        }
//
//    private fun setSourcePathView(path: String) {
//        viewModel?.selectedFileLiveData?.value = path
//
//    }
//    private fun newLeaveRequest() {
//        val btnsheet = layoutInflater.inflate(R.layout.leave_request, null)
//        newLeaveDialog = BottomSheetDialog(requireContext())
//
//        val leaveType = btnsheet.findViewById<AppCompatSpinner>(R.id.leaveType)
//        val desc = btnsheet.findViewById<EditText>(R.id.desc)
//        val startDate = btnsheet.findViewById<EditText>(R.id.startDate)
//        val endDate = btnsheet.findViewById<EditText>(R.id.endDate)
//        val leaveBal = btnsheet.findViewById<TextView>(R.id.leaveBal)
//        val attachCardView = btnsheet.findViewById<CardView>(R.id.attachCardView)
//        val remove = btnsheet.findViewById<ImageView>(R.id.remove)
//        leaveAttachmentFileTextView = btnsheet.findViewById<TextView>(R.id.attachmentFileTextView)
//        val submitBt = btnsheet.findViewById<AppCompatButton>(R.id.submitBt)
//        var typeList = ArrayList<String>()
//        var leave_type_id: Int? = 0
//
//        if (leaveTypeList.isNotEmpty()){
//            leaveBal.text = leaveTypeList[0].shortCode + "Left:" + leaveTypeList[0].balanceLeaves + " of " + leaveTypeList[0].noOfLeaves
//            leave_type_id = leaveTypeList[0].id
//
//            for (i in leaveTypeList.indices) {
//                typeList.add(leaveTypeList[i].title.toString())
//            }
//
//            val arrayAdapter =
//                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeList)
//            leaveType.adapter = arrayAdapter
//
//            leaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
//                    val short_code = leaveTypeList[i].shortCode
//                    val no_of_leaves = leaveTypeList[i].noOfLeaves
//                    val balance_leaves = leaveTypeList[i].balanceLeaves
//                    leave_type_id = leaveTypeList[i].id
//                    leaveBal.text = "$short_code Left:$balance_leaves of $no_of_leaves"
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//            }
//
//        }
//
//
//        val arrayAdapter =
//            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeList)
//        leaveType.adapter = arrayAdapter
//
//        leaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
//                val short_code = leaveTypeList[i].shortCode
//                val no_of_leaves = leaveTypeList[i].noOfLeaves
//                val balance_leaves = leaveTypeList[i].balanceLeaves
//                leave_type_id = leaveTypeList[i].id
//                leaveBal.text = "$short_code Left:$balance_leaves of $no_of_leaves"
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        }
//
//
//
//
//        startDate.setOnClickListener {
//            it.hideKeyboard()
//            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
//            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")
//
//            // Setting up the event for when ok is clicked
//            datePicker.addOnPositiveButtonClickListener {
//                val sdf = SimpleDateFormat(dateFormat, Locale.US)
//                fromDate = sdf.format(datePicker.selection?.first)
//                startDate.setText(fromDate)
//                toDate =  sdf.format(datePicker.selection?.second)
//                endDate.setText(toDate)
//
//                val startDate: Long? = datePicker.selection?.first
//                val endDate: Long? = datePicker.selection?.second
//
//                val msDiff = endDate!!.minus(startDate!!)
//                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)
//
//                val days = daysDiff + 1;
//
//                submitBt.text = "Submit($days day)"
//            }
//            // Setting up the event for when cancelled is clicked
//            datePicker.addOnNegativeButtonClickListener {
//
//            }
//            // Setting up the event for when back button is pressed
//            datePicker.addOnCancelListener {
//
//            }
//        }
//
//        endDate.setOnClickListener {
//            it.hideKeyboard()
//            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
//            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")
//
//            // Setting up the event for when ok is clicked
//            datePicker.addOnPositiveButtonClickListener {
//                val sdf = SimpleDateFormat(dateFormat, Locale.US)
//                fromDate = sdf.format(datePicker.selection?.first)
//                startDate.setText(fromDate)
//                toDate =  sdf.format(datePicker.selection?.second)
//                endDate.setText(toDate)
//
//
//                val startDate: Long? = datePicker.selection?.first
//                val endDate: Long? = datePicker.selection?.second
//
//                val msDiff = endDate!!.minus(startDate!!)
//                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)
//
//                val days = daysDiff + 1;
//
//                submitBt.text = "Submit($days day)"
//
//
//            }
//            // Setting up the event for when cancelled is clicked
//            datePicker.addOnNegativeButtonClickListener {
//
//            }
//            // Setting up the event for when back button is pressed
//            datePicker.addOnCancelListener {
//
//            }
//        }
//
//        attachCardView.setOnClickListener {
//            callFileAccessIntent()
//        }
//
//        remove.setOnClickListener {
//            viewModel?.clearAttachment()
//        }
//
//        submitBt.setOnClickListener {
//            if (leave_type_id!!.equals(null)) {
//                Toast.makeText(requireContext(), "Select Leave Type", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            val description = desc?.text?.toString()?.trim()
//            if (description!!.isEmpty()) {
//                Toast.makeText(requireContext(), "Add Description", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//
//            if (fromDate.isEmpty()) {
//                Toast.makeText(requireContext(), "Start date is not selected", Toast.LENGTH_LONG)
//                    .show()
//                return@setOnClickListener
//            }
//            if (toDate.isEmpty()) {
//                Toast.makeText(requireContext(), "End date is not selected", Toast.LENGTH_LONG)
//                    .show()
//                return@setOnClickListener
//            }
//
//            var applyLeave = ApplyLeave(leave_type_id, description, fromDate, toDate)
//
//            viewModel?.applyLeave(requireContext(), applyLeave)
//
//
//        }
//
//        newLeaveDialog.setContentView(btnsheet)
//        newLeaveDialog.show()
//
//    }
//
//
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        binding?.materialToolbar?.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        binding?.tabLay?.addTab(binding?.tabLay?.newTab()!!.setText("Leave"))
//        binding?.tabLay?.addTab(binding?.tabLay?.newTab()!!.setText("Document"))
//
//
//        val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
//
//
//
//        binding?.tabLay?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                binding?.viewPager?.currentItem = tab!!.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//        })
//
//        binding?.viewPager?.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                binding?.tabLay?.selectTab(binding?.tabLay?.getTabAt(position))
//
//            }
//        })
//
//
//
//        viewModel?.getLeaveTypes(requireContext())
//
//        viewModel?.getRequestTypes(requireContext())
//
//        var getRequests = GetRequests(20, 1)
//        viewModel?.getEmployeeRequests(requireContext(), getRequests)
//
//        viewModel?.getRequestSuccess?.observe(viewLifecycleOwner) {
//            binding?.viewPager?.adapter = RequestFragmentAdapter(
//                fragmentManager,
//                lifecycle,
//                it.data?.leaveRequests,
//                it.data?.otherRequests
//            )
//        }
//
//        viewModel?.leaveTypesData?.observe(viewLifecycleOwner) {
//            leaveTypeList = it.data as ArrayList<LeaveTypeData>
//        }
//
//        viewModel?.selectedFileLiveData?.observe(viewLifecycleOwner) {
//            if (attachmentFileTextView != null){
//                val fileName = AppUtils.getFileNameFromPath(it)
//                attachmentFileTextView?.text = fileName
//            }
//            if (leaveAttachmentFileTextView != null){
//                val fileName = AppUtils.getFileNameFromPath(it)
//                leaveAttachmentFileTextView?.text = fileName
//            }
//        }
//
//        viewModel?.requestTypesSuccess?.observe(viewLifecycleOwner) {
//            docTypeList = it.requestTypeItem as ArrayList<RequestTypeItem>
//        }
//
//        viewModel?.documentRequestSuccess?.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//            newDocDialog.dismiss()
//        }
//
//        viewModel?.documentRequestError?.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//        }
//
//        viewModel?.applyLeaveSuccess?.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//            newLeaveDialog.dismiss()
//
//        }
//
//        viewModel?.applyLeaveError?.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//        }
//
//
//    }
//
//    private fun View.hideKeyboard() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(windowToken, 0)
//    }
//
//
//}
