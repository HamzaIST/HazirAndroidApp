package technology.innovate.haziremployee.ui.manager.Addnewjob

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_new_job_post.*
import okhttp3.MultipartBody
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityAddNewJobPostBinding
import technology.innovate.haziremployee.rest.entity.addnewjobresponse.AddNewjobresponseModel
import technology.innovate.haziremployee.rest.entity.branchlistmodel.Branchlistmodel
import technology.innovate.haziremployee.rest.entity.countrylistmodel.CountrylistModel
import technology.innovate.haziremployee.rest.entity.currencylistmodel.Currencylistmodel
import technology.innovate.haziremployee.rest.entity.departmentlistmodel.Depalrtmentlistresponse
import technology.innovate.haziremployee.rest.entity.designationlist.Designationlistmodel
import technology.innovate.haziremployee.rest.entity.jobcompanymodel.Jobcomapanymodel
import technology.innovate.haziremployee.rest.entity.newjobpostrequestmodel.Newjobpostrequestmodel
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Data
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Prerequistquestionmodel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.applyjobform.*
import technology.innovate.haziremployee.ui.applyjobform.filterjoblist.DesigbationlistAdaptor
import technology.innovate.haziremployee.ui.jobpost.Jobpostlistviewmodel
import technology.innovate.haziremployee.ui.manager.Managerjobpostlist
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.io.File
import java.util.*


@AndroidEntryPoint
class AddNewJobPost : AppCompatActivity() {
    var isAllFieldsChecked = false
    private lateinit var pickFillTypeDoc: ActivityResultLauncher<String>
    lateinit var courseList: ArrayList<Questiondetail>

    var applicationtype:String?=""
    var id:String?=""
    private var files: MultipartBody.Part? = null
    private lateinit var viewBinding: ActivityAddNewJobPostBinding
    private val viewModel by viewModels<JobQuestionViewModel>()
    private lateinit var addNewJobPostAdaptor: AddnewjobpostAdaptor
    private lateinit var selectedQuestionAdaptor: SelectedQuestionAdaptor
    private val uploadviewModel by viewModels<UploaddocViewModel>()
    private lateinit var msg: String
    private var type: String? = "text"
    var path: File? = null
    var dialog: Dialog? = null
    var countryid:Int=0
    var arrayList: ArrayList<String>? = null
    var selectedqid: ArrayList<Int>? = null
    private var body: MultipartBody.Part? = null
    private lateinit var countrylistAdaptor: CountrylistAdaptor
    private lateinit var currencylistAdaptor: CurrencylistAdaptor
    private val jobpostlistviewmodel: Jobpostlistviewmodel by viewModels()

    private  var statusid:String=""
    private  var categoryid:Int=0
    private  var departmentid:Int=0
    private  var designationid:Int=0
    private  var jobtypeid:Int=0
    private  var companyid:Int=0
    private  var currencyid:Int=0
    private  var branchid:Int=0
    private  var roundid:Int=0

    var categoryarray: ArrayList<String>? = null
    var jobtypearray: ArrayList<String>? = null
    var statusarray: ArrayList<String>? = null
    var roundarray: ArrayList<Int>? = null

    var selectedquestion: ArrayList<Data>?=null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_job_post)

        courseList= ArrayList()

        selectedquestion= ArrayList()
        jobtypearray= ArrayList()
        jobtypearray!!.add("Select")
        jobtypearray!!.add("Full Time")
        jobtypearray!!.add("Part Time")


        search()
        setupRecyclerView()


        viewBinding.add.setOnClickListener {
            Log.e("selectedquestion",selectedquestion.toString())

            viewBinding.selectedcardview.visibility=View.VISIBLE
            val adapter = SelectedQuestionAdaptor(selectedquestion!!,viewBinding.selectedcardview)
            viewBinding.questionrecyclerView.adapter=adapter
            viewBinding.questionrecyclerView.visibility=View.VISIBLE


        }


        categoryarray= ArrayList()
        categoryarray!!.add("Select")
        categoryarray!!.add("wfh")
        categoryarray!!.add("wfo")
        categoryarray!!.add("hybrid")



        statusarray= ArrayList()
        selectedqid= ArrayList()
        statusarray!!.add("Select")
        statusarray!!.add("open")
        statusarray!!.add("closed")
        statusarray!!.add("hold")

        roundarray= ArrayList()
        roundarray!!.add(1)
        roundarray!!.add(2)
        roundarray!!.add(3)
        roundarray!!.add(4)
        roundarray!!.add(5)
        roundarray!!.add(6)
        roundarray!!.add(7)
        roundarray!!.add(8)
        roundarray!!.add(9)
        roundarray!!.add(10)


        ////


        val statusadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, statusarray!!)
           viewBinding.statusspinner!!.adapter = statusadapter


        viewBinding.statusspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (statusadapter.getItem(position).toString()=="open")
                {
                    statusid="1"
                }
                else if(statusadapter.getItem(position).toString()=="closed")
                {
                    statusid="2"
                }

                else if (statusadapter.getItem(position).toString()=="hold")
                {
                    statusid="3"
                }
                else
                {
                    statusid=""
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                statusid=""
            }
        }

        /////

        val roundadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, roundarray!!)
        viewBinding.roundspinner!!.adapter = roundadapter


        viewBinding.roundspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                roundid= roundadapter.getItem(position)!!
                Log.e("status",roundadapter.getItem(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }

        ///

        val jobtypeadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, jobtypearray!!)
        viewBinding.jobtypespinner!!.adapter = jobtypeadapter




        viewBinding.jobtypespinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (jobtypeadapter.getItem(position).toString()=="Full Time")
                {
                    jobtypeid=1
                }
                else if(jobtypeadapter.getItem(position).toString()=="Part Time")
                {
                    jobtypeid=2
                }




            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }

        /////


        viewBinding.searchview.doOnTextChanged { text, start, before, count ->
            Log.e("textdata","start"+start+"before"+before)
            if (text!=null)
            {

                viewBinding.recyclerView.visibility=View.VISIBLE
                val newList = addNewJobPostAdaptor.differ.currentList.filter { it.question?.contains(text,true) == true

                }
                Log.e("newlist", newList.toString() )
                addNewJobPostAdaptor.differ.submitList(newList)
                if(text.isNullOrBlank() || addNewJobPostAdaptor.differ.currentList.isEmpty()) addNewJobPostAdaptor.differ.submitList(viewModel.storePickupList)
                viewBinding.recyclerView.isVisible = !text.isNullOrBlank()

                if (newList.isEmpty())
                {
                    viewBinding.recyclerView.visibility=View.GONE
                }
            }
            else
            {

            }

        }

        ///

        val categoryadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, categoryarray!!)
          viewBinding.jobcategoryspinner!!.adapter = categoryadapter


        viewBinding.jobcategoryspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                if (categoryadapter.getItem(position).toString()=="wfh")
                {
                    categoryid=1
                }
                else if(categoryadapter.getItem(position).toString()=="wfo")
                {
                    categoryid=2
                }

                else if(categoryadapter.getItem(position).toString()=="hybrid")
                {
                    categoryid=3
                }
                else
                {
                    categoryid=0
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }
        ////

        loaddesignation()
        loaddepartment()
        loadbranchlist()
        loadcompanylist()

        countrylistAdaptor = CountrylistAdaptor()
        currencylistAdaptor = CurrencylistAdaptor()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                startActivity(Intent(this@AddNewJobPost, Managerjobpostlist::class.java))
                finish()
            }
        })
        id= intent.getStringExtra("id")
        Log.e("asdasdas", BaseApplication.QuestionObj.detailid.toString())
        try {

            //loadAllPostsFromRemote(id.toString())
            loadcountrylist()
            loadcurrencylist()


        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }






        viewBinding.materialToolbar.setOnClickListener {
            var intent= Intent(this, Managerjobpostlist::class.java)
            startActivity(intent)
            finish()
        }


        viewBinding.country.setOnClickListener {
            dialog= Dialog(this)
            dialog!!.setContentView(R.layout.dialog_searchable_spinner)
            dialog!!.window?.setLayout(650,800)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog!!.show()

            val recyclerViewnew= dialog!!.findViewById<RecyclerView>(R.id.listview)




            recyclerViewnew.apply {
                adapter = countrylistAdaptor

                countrylistAdaptor.setOnRowClickListener {
                    countryid=it.id
                    viewBinding.country.setText(it.title)
                    dialog!!.dismiss()
                }
            }

        }

        viewBinding.jobcurrency.setOnClickListener {
            dialog= Dialog(this)
            dialog!!.setContentView(R.layout.dialogcurrencylist)
            dialog!!.window?.setLayout(650,800)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog!!.show()

            val recyclerViewnew= dialog!!.findViewById<RecyclerView>(R.id.listview)

            recyclerViewnew.apply {
                adapter = currencylistAdaptor

                currencylistAdaptor.setOnRowClickListener {
                    currencyid=it.id


                    viewBinding.jobcurrency.setText(it.currency)
                    dialog!!.dismiss()
                }
            }

        }
        viewBinding.materialToolbar.setOnClickListener {
            val intent = Intent(this@AddNewJobPost, Managerjobpostlist::class.java)
            intent.putExtra("Username", intent.getStringExtra("Username").toString())
            startActivity(intent)
            finish()
        }





        viewBinding.submitMaterialButton.setOnClickListener {
            Log.e("questionlist", selectedquestion.toString())
            val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
            Log.e("selectedid",BaseApplication.QuestionObj.selectedquestionid.toSet().toString())
            isAllFieldsChecked = CheckAllFields()
            if(isAllFieldsChecked) {

                val newjobpostrequestmodel = Newjobpostrequestmodel(
                    departmentId = departmentid,
                    designationId = designationid,
                    branchId = branchid,
                    companyId = companyid,
                    jobPostName = viewBinding.jobname.text?.trim().toString(),
                    minExperience = viewBinding.minexperience.text?.trim().toString(),
                    maxExperience = viewBinding.maxexperience.text?.trim().toString(),
                    minSalary = viewBinding.minsalary.text?.trim().toString(),
                    maxSalary = viewBinding.maxsalary.text?.trim().toString(),
                    isDisplaySalaryJobPage = 0,
                    currencyId = currencyid,
                    jobDescription =viewBinding.jobdescription.text?.trim().toString(),
                    noOfPositions = viewBinding.noofposition.text?.trim().toString(),
                    jobType = jobtypeid,
                    jobCategory = categoryid,
                    jobResponsibilities = viewBinding.jobresponsibility.text?.trim().toString(),
                    noOfRounds=roundid,
                    countryId = countryid,
                    requisitesQuestions = BaseApplication.QuestionObj.selectedquestionid.toSet().toList(),

                )


                viewModel.addnewjobrequest(newjobpostrequestmodel)
                viewModel.addnewjob.observe(this, postsObserverapplyjob)
            }

        }





    }




    private var postsObserverapplyjob: Observer<DataState<AddNewjobresponseModel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {


                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                    Log.e("applyjob",it.item.message)
//                    validatePostsData(it.item)

                    Toast.makeText(this,it.item.message,Toast.LENGTH_LONG).show()
                    val intent=Intent(this,Managerjobpostlist::class.java)
                    startActivity(intent)
                    finish()


                }
                is DataState.Error -> {

                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
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


    private  fun CheckAllFields(): Boolean {
        if (viewBinding.jobname.text?.trim()!!.isEmpty())
        {
            viewBinding.jobname.setError("This field is required");
            return false
        }
        if (designationid==0)
        {
            viewBinding.designationerror.visibility=View.VISIBLE
            return false
        }
        else
        {
            viewBinding.designationerror.visibility=View.GONE
        }
        if (departmentid==0)
        {
            viewBinding.departmenterror.visibility=View.VISIBLE
            return false

        }
        else
        {
            viewBinding.departmenterror.visibility=View.GONE
        }


        if (branchid==0)
        {
            viewBinding.brancherror.visibility=View.VISIBLE
            return false
        }
        else
        {
            viewBinding.brancherror.visibility=View.GONE
        }

        if (jobtypeid==0)
        {
            viewBinding.jobtypeerror.visibility=View.VISIBLE
            return false
        }
        else
        {
            viewBinding.jobtypeerror.visibility=View.GONE
        }


        if (companyid==0)
        {
            viewBinding.companyerror.visibility=View.GONE
            return false
        }
        else
        {
            viewBinding.companyerror.visibility=View.GONE
        }

        if (categoryid==0)
        {
            viewBinding.jobcategoryerror.visibility=View.GONE
            return false
        }
        else
        {
            viewBinding.jobcategoryerror.visibility=View.GONE
        }

        if (roundid==0)
        {
            viewBinding.roundspinnererror.visibility=View.GONE
            return false
        }
        else
        {
            viewBinding.roundspinnererror.visibility=View.GONE
        }

        if (BaseApplication.QuestionObj.selectedquestionid.toSet().toList().isEmpty())
        {
            Toast.makeText(this,"Please Select Atleast One Question",Toast.LENGTH_LONG).show()
            return false
        }


        return true

    }



    fun search()
    {
        viewModel.prereqquestion()


        viewModel.prereqquestion.observe(this) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("department",it.item.toString())

                    viewModel.storePickupList=it.item.data
                    addNewJobPostAdaptor.differ.submitList(it.item.data)
                    viewBinding.recyclerView.visibility=View.GONE

                    addNewJobPostAdaptor.onRowClickListener {
                        viewModel.addprequestion= listOf(it)
                    viewBinding.searchview.setText(it.question)
                    selectedquestion!!.add(Data(it.id,it.question))

                    }


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
    private fun setupRecyclerView() {
        addNewJobPostAdaptor = AddnewjobpostAdaptor(this)

        viewBinding.recyclerView.adapter = addNewJobPostAdaptor
    }







    private fun loadcountrylist() {
        viewModel.countrylist()
        viewModel.countrylist.observe(this, postsObservercountrylist)

    }


    private fun loadcurrencylist() {
        viewModel.currencylist()
        viewModel.currencylist.observe(this, postsObservercurrencylist)

    }



    private var postsObservercountrylist: Observer<DataState<CountrylistModel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {

                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()



//                    validatePostsData(it.item)
                    countrylistAdaptor.differ.submitList(it.item.data)

                }
                is DataState.Error -> {

                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
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




    private var postsObservercurrencylist: Observer<DataState<Currencylistmodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {

                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                    Log.e("countrylist",it.item.data.toString())
//                    validatePostsData(it.item)
                    currencylistAdaptor.differ.submitList(it.item.data)

                }
                is DataState.Error -> {

                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
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




    private var postsObserver: Observer<DataState<Prerequistquestionmodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                    Log.e("dataasd",it.item.data.toString())
                    //validatePostsData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
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


    private fun loadbranchlist() {
        jobpostlistviewmodel.branchlist()
//        jobpostlistviewmodel.departmentRequestTypes.observe(this, designationlistobserver)

        jobpostlistviewmodel.branchlistRequestTypes.observe(
            this
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("designationdata",it.item.toString())
                    branchTypesData(it.item)
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


    private fun loadcompanylist() {
        jobpostlistviewmodel.companylist()
//        jobpostlistviewmodel.departmentRequestTypes.observe(this, designationlistobserver)

        jobpostlistviewmodel.companylistRequestTypes.observe(
            this
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Log.e("designationdata",it.item.toString())
                    companyTypesData(it.item)
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



    private fun loaddesignation() {
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
                    designationTypesData(it.item)
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
    private fun designationTypesData(
        response: Designationlistmodel,
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter = DesigbationlistAdaptor(this, response.data)
                viewBinding.designationspinner.apply {
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
                                    designationid=spinnerAdapter.result[p2-1]?.id!!
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
                viewBinding.designationspinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }


    }



    @SuppressLint("SetTextI18n")
    private fun branchTypesData(
        response: Branchlistmodel,
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter = BranchlistAdaptor(this, response.data)
                viewBinding.branchspinner.apply {
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
                                    branchid=spinnerAdapter.result[p2-1]?.id!!
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
                    BranchlistAdaptor(this, Collections.emptyList())
                viewBinding.branchspinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }


    }


    @SuppressLint("SetTextI18n")
    private fun companyTypesData(
        response: Jobcomapanymodel,
    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter = CompanylistAdaptor(this, response.data)
                viewBinding.companyspinner.apply {
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
                                    companyid=spinnerAdapter.result[p2-1]?.id!!
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
                    CompanylistAdaptor(this, Collections.emptyList())
                viewBinding.companyspinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }


    }


    private fun loaddepartment() {
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
                    validateLeaveTypesData(it.item)
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

    ) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                val spinnerAdapter =
                    DepartmentlistSpinnerAdaptor(this, response.data)
                viewBinding.departmentspinner.apply {
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
                                    departmentid=spinnerAdapter.result[p2-1]?.id!!
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
                departmentspinner.apply {
                    adapter = equipmentsSpinnerAdapter
                }
            }

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }


    }

}