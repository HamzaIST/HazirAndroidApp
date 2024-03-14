package technology.innovate.haziremployee.ui.manager

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.BuildConfig
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityManagerjobpostlistBinding
import technology.innovate.haziremployee.rest.entity.departmentlistmodel.Depalrtmentlistresponse
import technology.innovate.haziremployee.rest.entity.designationlist.Designationlistmodel
import technology.innovate.haziremployee.rest.entity.jobtitle.DataModel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.applyjobform.DepartmentlistSpinnerAdaptor
import technology.innovate.haziremployee.ui.applyjobform.filterjoblist.DesigbationlistAdaptor
import technology.innovate.haziremployee.ui.jobpost.Jobpostlistviewmodel
import technology.innovate.haziremployee.ui.manager.Addnewjob.AddNewJobPost
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class Managerjobpostlist : AppCompatActivity() {
    private lateinit var viewBinding: ActivityManagerjobpostlistBinding
    private lateinit var jobPostAdaptor: ManagerJobPostAdaptor
    internal lateinit var dataModelArrayList: ArrayList<DataModel>

    private lateinit var departmentlistSpinnerAdaptor: DepartmentlistSpinnerAdaptor
    private lateinit var leaveBottomSheetDialog: BottomSheetDialog

    private  var statusid:Int?=null
    private  var categoryid:Int?=null
    private  var departmentid:Int?=null
    private  var designationid:Int?=null

    private  var designationidposition:Int?=null
    private  var departmentypeposition:Int?=null

    var categoryarray: ArrayList<String>? = null
    var statusarray: ArrayList<String>? = null
    private val jobpostlistviewmodel: Jobpostlistviewmodel by viewModels()
    @SuppressLint("NewApi", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_managerjobpostlist)


        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Managerjobpostlist, HomeActivity::class.java))
            finish()
        }

        viewBinding.addnewjob.setOnClickListener {
            startActivity(Intent(this@Managerjobpostlist, AddNewJobPost::class.java))
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Managerjobpostlist, HomeActivity::class.java))
                finish()
            }
        })
        val original = BuildConfig.BASE_URL
        SessionManager.init(BaseApplication.instance)

        setupRecycler()
        lifecycleScope.launch {
            jobpostlistviewmodel.getmanagerjobList(null,null,null,null).distinctUntilChanged().collectLatest{
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
        leaveBottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)



        categoryarray= ArrayList()
        categoryarray!!.add("Select Category")
        categoryarray!!.add("wfh")
        categoryarray!!.add("wfo")
        categoryarray!!.add("hybrid")


        statusarray= ArrayList()
        statusarray!!.add("Select Status")
        statusarray!!.add("open")
        statusarray!!.add("closed")
        statusarray!!.add("hold")

        val leaveTypesSpinner=leaveBottomSheetDialog.findViewById<DynamicWidthSpinner>(R.id.sppinner)
        val leaveBalanceTextView = leaveBottomSheetDialog.findViewById<TextView>(R.id.spinnertext)
        val categoryspinner = leaveBottomSheetDialog.findViewById<DynamicWidthSpinner>(R.id.categoryspinner)
        val statusspinner = leaveBottomSheetDialog.findViewById<DynamicWidthSpinner>(R.id.statusspinner)
        val searchbutton = leaveBottomSheetDialog.findViewById<Button>(R.id.searchfilter)
        val clear = leaveBottomSheetDialog.findViewById<Button>(R.id.clear)




        searchbutton!!.setOnClickListener {

            lifecycleScope.launch {
                jobpostlistviewmodel.getmanagerjobList(departmentid=departmentid,designationid = designationid,categoryid=categoryid,status=statusid).distinctUntilChanged().collectLatest{
                    jobPostAdaptor.notifyDataSetChanged()
                    jobPostAdaptor.submitData(it)
                    jobPostAdaptor.notifyDataSetChanged()

                    try {
                        Log.e("joblistdataqasd",it.toString())
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
            departmentypeposition=null
            designationid=null
            designationid=null
            categoryid=null
            statusid=null
            lifecycleScope.launch {
                jobpostlistviewmodel.getmanagerjobList(null, null,null,null).distinctUntilChanged().collectLatest{
                    jobPostAdaptor.notifyDataSetChanged()
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

        val designationtypespinner=leaveBottomSheetDialog.findViewById<DynamicWidthSpinner>(R.id.designationsppinner)
        val designationTextView=leaveBottomSheetDialog.findViewById<TextView>(R.id.designationspinnertext)


        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, categoryarray!!)
        categoryspinner!!.adapter = adapter
        if (categoryid!=null)
        {
            categoryspinner!!.setSelection(categoryid!!)
        }

        val statusadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, statusarray!!)
        statusspinner!!.adapter = statusadapter

        if (statusid!=null)
        {
            statusspinner!!.setSelection(statusid!!)
        }

        statusspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (statusadapter.getItem(position).toString()=="open")
                {
                    statusid=1
                }
                else if(statusadapter.getItem(position).toString()=="closed")
                {
                    statusid=2
                }

                else if (statusadapter.getItem(position).toString()=="hold")
                {
                    statusid=3
                }
                else
                {
                    statusid=null
                }


                Log.e("status",statusadapter.getItem(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                statusid=null
            }
        }



        categoryspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (adapter.getItem(position).toString()=="wfh")
                {
                    categoryid=1
                }
                else if(adapter.getItem(position).toString()=="wfo")
                {
                    categoryid=2
                }

                else if(adapter.getItem(position).toString()=="hybrid")
                {
                    categoryid=3
                }
                else
                {
                    categoryid=null
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                categoryid=null
            }
        }



        loaddepartment(leaveTypesSpinner!!,leaveBalanceTextView!!)
        loaddesignation(designationtypespinner!!,designationTextView!!)

        leaveBottomSheetDialog.show()


    }


    private fun setupRecycler() {
        jobPostAdaptor = ManagerJobPostAdaptor(this,jobpostlistviewmodel)
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
            } else {
                viewBinding.errorLayout.errorText.text = "No Data Found"
                viewBinding.errorLayout.root.hide()
            }
        }

    }



    private fun loaddepartment(
        spinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView
    ) {
        jobpostlistviewmodel.departmentlist(SessionManager.encryptedorgid.toString())
//        jobpostlistviewmodel.departmentRequestTypes.observe(this, designationlistobserver)

        jobpostlistviewmodel.departmentRequestTypes.observe(
            this
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("department",it.item.toString())
                    validateLeaveTypesData(it.item, spinner, leaveBalanceTextView)
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


    private fun loaddesignation(
        spinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView
    ) {
        jobpostlistviewmodel.designationlist(SessionManager.encryptedorgid.toString())
//        jobpostlistviewmodel.departmentRequestTypes.observe(this, designationlistobserver)

        jobpostlistviewmodel.designationRequestTypes.observe(
            this
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("designationdata",it.item.toString())
                    designationTypesData(it.item, spinner, leaveBalanceTextView)
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
        leaveBalanceTextView: TextView
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter =
                    DepartmentlistSpinnerAdaptor(this, response.data)
                leaveTypesSpinner.apply {
                    adapter = spinnerAdapter
                    if (departmentypeposition!=null)
                    {
                        leaveTypesSpinner.setSelection(departmentypeposition!!)
                        Log.e("designationid",departmentypeposition.toString())
                    }
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
                                    departmentid=spinnerAdapter.result[p2-1]?.id
                                    Log.e("data",spinnerAdapter.result[p2-1]?.id.toString())
                                    departmentypeposition=leaveTypesSpinner.getPositionForView(p1)

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
                    if (designationid!=null)
                    {
                        designationtypeSpinner.setSelection(designationidposition!!)

                        Log.e("designationid",designationid.toString())
                    }
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
                                    designationid=spinnerAdapter.result[p2-1]?.id
                                    designationidposition=designationtypeSpinner.getPositionForView(p1)
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