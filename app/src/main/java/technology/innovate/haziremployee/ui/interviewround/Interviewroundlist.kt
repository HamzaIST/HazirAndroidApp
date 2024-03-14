package technology.innovate.haziremployee.ui.interviewround

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottominterviewlayout.*
import kotlinx.android.synthetic.main.interviewlistlayout.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.BuildConfig
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityInterviewroundlistBinding
import technology.innovate.haziremployee.rest.entity.departmentlistmodel.Depalrtmentlistresponse
import technology.innovate.haziremployee.rest.entity.designationlist.Designationlistmodel

import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.applyjobform.DepartmentlistSpinnerAdaptor
import technology.innovate.haziremployee.ui.applyjobform.filterjoblist.DesigbationlistAdaptor
import technology.innovate.haziremployee.ui.jobpost.Jobpostlistviewmodel

import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class Interviewroundlist : AppCompatActivity() {

    private lateinit var viewBinding: ActivityInterviewroundlistBinding
    private lateinit var jobPostAdaptor: InterviewroundlistAdaptor

    private lateinit var departmentlistSpinnerAdaptor: DepartmentlistSpinnerAdaptor
    private lateinit var leaveBottomSheetDialog: BottomSheetDialog

    private  var statusid:Int?=null
    private  var categoryid:String=""
    private  var interviewtypeid:Int?=null
    private  var designationid:String=""
    var interviewdateselected:String=""
    var newinterviewdate:Any=0

    var interviewtypearray: ArrayList<String>? = null
    var statusarray: ArrayList<String>? = null
    private val jobpostlistviewmodel: Jobpostlistviewmodel by viewModels()
    @SuppressLint("NewApi", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_interviewroundlist)


        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Interviewroundlist, HomeActivity::class.java))
            finish()
        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Interviewroundlist, HomeActivity::class.java))
                finish()
            }
        })
        val original = BuildConfig.BASE_URL
        SessionManager.init(BaseApplication.instance)

        setupRecycler()


        lifecycleScope.launch {
            jobpostlistviewmodel.getinterviewlist(0,0,null,0).distinctUntilChanged().collectLatest{
                jobPostAdaptor.submitData(it)
                try {
                    Log.e("joblistdata",it.toString())
                }
                catch (ex:Exception)
                {
                    ex.printStackTrace()
                }
            }
        }


        viewBinding.filters.setOnClickListener {
            newLeaveRequest()

        }

    }




    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun newLeaveRequest() {

        leaveBottomSheetDialog= BottomSheetDialog(this)
        leaveBottomSheetDialog.setContentView(R.layout.bottominterviewlayout)



        interviewtypearray= ArrayList()

        interviewtypearray!!.add("Select Interview Type")
        interviewtypearray!!.add("Show All")
        interviewtypearray!!.add("F2F")
        interviewtypearray!!.add("VideoCall")


        statusarray= ArrayList()

        statusarray!!.add("Select Status")
        statusarray!!.add("Show All")
        statusarray!!.add("Schedule")
        statusarray!!.add("Completed")
        statusarray!!.add("Postponed")
        statusarray!!.add("Cancelled")

        val interviewtypespinner=leaveBottomSheetDialog.findViewById<DynamicWidthSpinner>(R.id.interviewtypespinner)
        val interviewdate = leaveBottomSheetDialog.findViewById<TextView>(R.id.interviewdate)
        val interviewstatusspinner = leaveBottomSheetDialog.findViewById<DynamicWidthSpinner>(R.id.interviewstatusspinner)

        val searchbutton = leaveBottomSheetDialog.findViewById<Button>(R.id.searchfilter)
        val clear = leaveBottomSheetDialog.findViewById<Button>(R.id.clear)


        Log.e("interview date",interviewdateselected)
        if (interviewdateselected.isNotEmpty())
        {
            interviewdate?.setText(interviewdateselected)
        }

        interviewdate!!.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                interviewdate.text = sdf.format(datePicker.selection)
                interviewdateselected=sdf.format(datePicker.selection)

                newinterviewdate= datePicker.selection!!
            }
        }

        searchbutton!!.setOnClickListener {

            lifecycleScope.launch {
                Log.e("datessss",interviewdate.text.toString())
                jobpostlistviewmodel.getinterviewlist(jobapplicationid = 0, interviewtype =  statusid, interviewdate = interviewdateselected.trim().toString(), interviewstatus = interviewtypeid).distinctUntilChanged().collectLatest{

                    jobPostAdaptor.submitData(it)


                    try {
                        Log.e("joblistdata",it.toString())
                    }
                    catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }
                }
            }
            leaveBottomSheetDialog.dismiss()

        }

        clear!!.setOnClickListener {
            interviewdateselected=""
            interviewtypeid=null
            statusid=null
           // getinterviewlist(jobapplicationid = 0, interviewtype = 0, interviewdate = null, interviewstatus = 0)

            lifecycleScope.launch {
                jobpostlistviewmodel.getinterviewlist(0, 0, interviewdate = null,0).distinctUntilChanged().collectLatest{
                    jobPostAdaptor.submitData(it)
                    jobPostAdaptor.notifyDataSetChanged()

                    try {
                        Log.e("joblistdata",it.toString())
                    }
                    catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }
                }
            }
            leaveBottomSheetDialog.dismiss()

        }



        val interviewtypeadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, interviewtypearray!!)
        interviewtypespinner!!.adapter = interviewtypeadapter

        if (interviewtypeid!=null)
        {
            interviewtypespinner!!.setSelection(interviewtypeid!!)
        }
        interviewtypespinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (interviewtypeadapter.getItem(position).toString()=="F2F")
                {
                    interviewtypeid=1
                }
                else if(interviewtypeadapter.getItem(position).toString()=="VideoCall")
                {
                    interviewtypeid=2
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }

        //

        val statusadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, statusarray!!)
        interviewstatusspinner!!.adapter = statusadapter
        if (statusid!=null)
        {
            interviewstatusspinner!!.setSelection(statusid!!)
        }

        interviewstatusspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (statusadapter.getItem(position).toString()=="Completed")
                {
                    statusid=1
                }
                else if(statusadapter.getItem(position).toString()=="Schedule")
                {
                    statusid=2
                }
                else if(statusadapter.getItem(position).toString()=="Postponed")
                {
                    statusid=3
                }
                else if(statusadapter.getItem(position).toString()=="Cancelled")
                {
                    statusid=4
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }

            //




        leaveBottomSheetDialog.show()


    }


    private fun setupRecycler() {
        jobPostAdaptor = InterviewroundlistAdaptor(this,jobpostlistviewmodel)
        viewBinding.recyclerView.adapter = jobPostAdaptor
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        jobPostAdaptor.addLoadStateListener { loadState ->
            viewBinding.recyclerView.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            viewBinding.progressBar.isVisible=loadState.refresh is LoadState.Loading
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && jobPostAdaptor.itemCount < 1) {

                viewBinding.errorLayout.errorText.text = "No Data Found"
                viewBinding.errorLayout.root.show()
                viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
            }
            else
            {
                viewBinding.errorLayout.errorText.text = "No Data Found"
                viewBinding.errorLayout.root.hide()
            }

        }

    }



    private fun loadinterviewtype(
        spinner: DynamicWidthSpinner,
    ) {
        jobpostlistviewmodel.departmentlist(SessionManager.encryptedorgid.toString())

        jobpostlistviewmodel.departmentRequestTypes.observe(
            this
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("department",it.item.toString())
                    validateLeaveTypesData(it.item, spinner)
                }
                is DataState.Error -> {
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (this as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }


    }





    @SuppressLint("SetTextI18n")
    private fun validateLeaveTypesData(
        response: Depalrtmentlistresponse,
        leaveTypesSpinner: DynamicWidthSpinner,
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter =
                    DepartmentlistSpinnerAdaptor(this, response.data)
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
                                //    leaveBalanceTextView.text=spinnerAdapter.result[p2-1]!!.title
                                //   leaveTypesSpinner.textView.setText(spinnerAdapter.result[p2]!!.title)

                                try {
                                    interviewtypeid=spinnerAdapter.result[p2-1]?.id
                                    Log.e("data",spinnerAdapter.result[p2-1]?.id.toString())
                                }
                                catch (ex:java.lang.Exception)
                                {
                                    ex.printStackTrace()
                                }


                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }
                        }
                }

            } else {
                val equipmentsSpinnerAdapter =
                    DepartmentlistSpinnerAdaptor(this, Collections.emptyList())
                leaveTypesSpinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }


    }



    @SuppressLint("SetTextI18n")
    private fun designationTypesData(
        response: Designationlistmodel,
        designationtypeSpinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter = DesigbationlistAdaptor(this, response.data)
                designationtypeSpinner.apply {
                    adapter = spinnerAdapter
                    onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                //    leaveBalanceTextView.text=spinnerAdapter.result[p2-1]!!.title
                                //   leaveTypesSpinner.textView.setText(spinnerAdapter.result[p2]!!.title)

                                try {
                                    designationid=spinnerAdapter.result[p2-1]?.id.toString()
                                    Log.e("data",spinnerAdapter.result[p2-1]?.id.toString())
                                }
                                catch (ex:java.lang.Exception)
                                {
                                    ex.printStackTrace()
                                }


                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }
                        }
                }

            } else {
                val equipmentsSpinnerAdapter =
                    DesigbationlistAdaptor(this, Collections.emptyList())
                designationtypeSpinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }
    }



}