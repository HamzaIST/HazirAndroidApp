package technology.innovate.haziremployee.ui.applyjobform

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.braver.tool.picker.BraverDocPathUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_applyjobform.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityApplyjobformBinding
import technology.innovate.haziremployee.rest.entity.countrylistmodel.CountrylistModel
import technology.innovate.haziremployee.rest.entity.currencylistmodel.Currencylistmodel
import technology.innovate.haziremployee.rest.entity.jobapplicationrequest.JobApplicationRequest
import technology.innovate.haziremployee.rest.entity.jobapplicationresponse.Jobapplicationresponse
import technology.innovate.haziremployee.rest.entity.jobquestions.Jobquestionmodel
import technology.innovate.haziremployee.rest.entity.resumeuploadmodel.ResumeuploadModel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.jobpost.Jobpost
import technology.innovate.haziremployee.ui.jobpostdetail.Jobpostdetail
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.AppUtils
import technology.innovate.haziremployee.utils.CustomDialog
import java.io.File
import java.lang.reflect.Type
import java.util.*
import java.util.regex.Pattern


@AndroidEntryPoint
class Applyjobform : AppCompatActivity() {
    var isAllFieldsChecked = false
    private lateinit var pickFillTypeDoc: ActivityResultLauncher<String>
    lateinit var courseList: ArrayList<Questiondetail>

    var applicationtype:String?=""
    var id:String?=""
    private var files: MultipartBody.Part? = null
    private lateinit var viewBinding: ActivityApplyjobformBinding
    private val viewModel by viewModels<JobQuestionViewModel>()
    private lateinit var jobQuestionAdaptor: JobQuestionAdaptor
    private val uploadviewModel by viewModels<UploaddocViewModel>()
    private lateinit var msg: String
    private var type: String? = "text"
    var path: File? = null
    var dialog: Dialog? = null
    var countryid:Int=0
    var resumename:String=""
    var arrayList: ArrayList<String>? = null
    private var body: MultipartBody.Part? = null
    private lateinit var countrylistAdaptor: CountrylistAdaptor
    private lateinit var currencylistAdaptor: CurrencylistAdaptor

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_applyjobform)

        courseList= ArrayList()


        BaseApplication.QuestionObj.postcodeList.clear()

        countrylistAdaptor = CountrylistAdaptor()
        currencylistAdaptor = CurrencylistAdaptor()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                startActivity(Intent(this@Applyjobform, Jobpostdetail::class.java))
                finish()
            }
        })
        id= intent.getStringExtra("id")
        Log.e("asdasdas",BaseApplication.QuestionObj.detailid.toString())
        try {
            initNotificationsRecyclerList()
            loadAllPostsFromRemote(id.toString())
            loadcountrylist()
            loadcurrencylist()

        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }


        viewBinding.resume.setOnClickListener {
            checkPermissionAndOpenPicker()
        }


        viewBinding.jobcurrency.setOnClickListener {


        }




        viewBinding.materialToolbar.setOnClickListener {
            val intent=Intent(this,Jobpostdetail::class.java)
            startActivity(intent)
            finish()

        }
        viewBinding.applicationtype.setOnClickListener {
            dialog=Dialog(this)
            dialog!!.setContentView(R.layout.dialogsource)
            dialog!!.window?.setLayout(650,250)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            arrayList = ArrayList()
            arrayList!!.add("Internal")
            arrayList!!.add("External")
            dialog!!.show()

            val listView= dialog!!.findViewById<ListView>(R.id.justlist)
            val arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,
               arrayList!!
           )
            listView.adapter=arrayAdapter
            listView.setOnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                // textview.setText(adapter.getItem(position))
                viewBinding.applicationtype.setText(arrayAdapter.getItem(position))
                // Dismiss dialog
                dialog!!.dismiss()
            }
        }

        viewBinding.country.setOnClickListener {
            dialog=Dialog(this)
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
            dialog=Dialog(this)
            dialog!!.setContentView(R.layout.dialogcurrencylist)
            dialog!!.window?.setLayout(650,800)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog!!.show()

            val recyclerViewnew= dialog!!.findViewById<RecyclerView>(R.id.listview)

            recyclerViewnew.apply {
                adapter = currencylistAdaptor

                currencylistAdaptor.setOnRowClickListener {

                    viewBinding.jobcurrency.setText(it.currency)
                    dialog!!.dismiss()
                }
            }

        }
        viewBinding.materialToolbar.setOnClickListener {
            val intent = Intent(this@Applyjobform, Jobpostdetail::class.java)
            intent.putExtra("Username", intent.getStringExtra("Username").toString())
            startActivity(intent)
            finish()
        }



//        viewBinding.nextbutton.setOnClickListener {
//            isAllFieldsChecked = CheckAllFields()
//            if(isAllFieldsChecked) {
//                            viewBinding.form.visibility= View.GONE
//            viewBinding.questionform.visibility=View.VISIBLE
//            viewBinding.submitMaterialButton.visibility=View.VISIBLE
//            }
//
////            viewBinding.form.visibility=View.GONE
////            viewBinding.questionform.visibility=View.VISIBLE
////            viewBinding.submitMaterialButton.visibility=View.VISIBLE
//        }


        viewBinding.submitMaterialButton.setOnClickListener {

            Log.e("questionlist", BaseApplication.QuestionObj.postcodeList.toString())
            val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)


            // creating a variable for gson.
            val gson = Gson()
            val json: String = gson.toJson(BaseApplication.QuestionObj.postcodeList)

            // below line is to get to string present from our
            // shared prefs if not present setting it as null.
      //      val json = sharedPreferences.getString("courses", "")

            // below line is to get the type of our array list.
            val type: Type = object : TypeToken<ArrayList<Questiondetail?>?>() {}.type

            // in below line we are getting data from gson
            // and saving it to our array list

                try {
                    courseList = gson.fromJson<Any>(json, type) as ArrayList<Questiondetail>
                    Log.e("rtyuio",courseList.toString())

                }
                    catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }

            isAllFieldsChecked = CheckAllFields()
            if(isAllFieldsChecked) {
               // Toast.makeText(this,"Submitted successfully",Toast.LENGTH_LONG).show()

                val jobApplicationRequest = JobApplicationRequest(
                    jobPostId = id.toString(),
                    fullName = viewBinding.fullname.text?.trim().toString(),
                    briefProfileSummary = viewBinding.profilebrief.text?.trim().toString(),
                    email = viewBinding.email.text?.trim().toString(),
                    phoneNumber = viewBinding.mobile.text?.trim().toString(),
                    education = viewBinding.highesteducation.text?.trim().toString(),
                    university = viewBinding.educationuniversity.text?.trim().toString(),
                    totalExperienceMonth = viewBinding.totalexperience.text?.trim().toString(),
                    relavantExperienceMonth = viewBinding.relaventexpereince.text?.trim().toString(),
                    countryId = countryid.toString(),
                    salaryExpectation = viewBinding.salaryexpected.text?.trim().toString(),
                    noticePeriodDays = viewBinding.noticeperiod.text?.trim().toString(),
                    currentCompanyName = viewBinding.currentcompany.text?.trim().toString(),
                    salaryExpectationCurrency = viewBinding.jobcurrency.text.trim().toString(),
                    resumeFileName =resumename,
                    jobQuestionAnswer = courseList,
                    externalSource = viewBinding.externalsource.text?.trim().toString()
                )
                viewModel.applyjob(jobApplicationRequest)
                viewModel.applyjob.observe(this, postsObserverapplyjob)




            }

        }




        pickFiles()
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
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkFilePermission()
        } else {
            openAttachments()
        }
    }


    private fun checkFilePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                setScreen("file")

            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            else -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setScreen("file")

            } else {
                //checkFilePermission()
                setScreen("file")

            }
        }


    private fun setScreen(section: String) {
        when (section) {
            "file" -> {
                pickFillTypeDoc.launch("application/pdf")
            }
            "pickImage" -> {
                val pickIntent =
                    Intent(Intent.ACTION_PICK)
                pickIntent.type = "image/* video/*"
                startActivityForResult(pickIntent, 301)

            }

            "location" -> {

            }
        }

    }


    @SuppressLint("SuspiciousIndentation")
    private fun pickFiles() {
        pickFillTypeDoc =
            registerForActivityResult(ActivityResultContracts.GetContent())
            { uri: Uri? ->

                if (uri == null) return@registerForActivityResult


                        if (uri != null) {

                            this.contentResolver.openInputStream(uri)
                                ?.use { inputStream ->

                                    //file =
                                    type = "document"
                                    path = File(uri.path!!)
                                    msg = try{
                                        uri.pathSegments[1].split("/")[1]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        "${System.currentTimeMillis().toString().take(5)}.pdf"
                                    }
                                    Log.e("FIlePath", msg)
                                    //viewBinding.resume.setText(msg.trim().toString())
                                    //requireActivity().invalidateOptionsMenu()
                                    val requestFile: RequestBody = inputStream.readBytes()
                                        .toRequestBody("*/*".toMediaTypeOrNull())
                                    val data = MultipartBody.Part.createFormData(
                                        "file_name",
                                        msg,
                                        requestFile
                                    )
                                    files = data
                                    val d1 = "Emir".toRequestBody(MultipartBody.FORM)
                                    Log.e("filename",d1.toString())

                                    uploadviewModel.uploadresume(

                                        file_name =  files ?: body)
                                    uploadviewModel.notifications.observe(this, postsObserverupload)

                                    }



                                    }
                                }

    }


    private  fun CheckAllFields(): Boolean {
        if (viewBinding.fullname.length()==0)
        {
            viewBinding.fullname.setError("This field is required");
            return false
        }
        if (viewBinding.email.length()==0)
        {
            viewBinding.email.setError("This field is required");
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(viewBinding.email.text?.trim().toString()).matches())
        {
            viewBinding.email.setError("Please Enter Valid Email");
            return false
        }


        if (viewBinding.mobile.length()<9)
        {
            viewBinding.mobile.setError("Please Check your mobile number");
            return false
        }


        if (viewBinding.resume.length()==0)
        {
            viewBinding.resume.setError("This field is required");
            return false
        }


        return true

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

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isEmailValid(email: String): Boolean {
        val isValid: Boolean
        //val expression = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        val expression = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        val inputStr: CharSequence = email
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(inputStr)
        isValid = matcher.matches()
        return isValid
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
                           this,
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
        //uploadviewModel.selectedFileLiveData?.value = path
    }

    private fun initNotificationsRecyclerList() {
        viewBinding.recyclerView.initRecyclerView(
            DefaultItemAnimator(), LinearLayoutManager(this)
        )
        jobQuestionAdaptor = JobQuestionAdaptor(this, listOf())
        viewBinding.recyclerView.adapter = jobQuestionAdaptor
    }
    private fun loadAllPostsFromRemote(id:String) {
        viewModel.jobquestion(id)
        viewModel.notifications.observe(this, postsObserver)

    }

    private fun loadcountrylist() {
        viewModel.countrylist()
        viewModel.countrylist.observe(this, postsObservercountrylist)
    }


    private fun loadcurrencylist() {
        viewModel.currencylist()
        viewModel.currencylist.observe(this, postsObservercurrencylist)

    }




    private var postsObserverupload: Observer<DataState<ResumeuploadModel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {


                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                 //   Toast.makeText(this,it.item.data,Toast.LENGTH_LONG).show()
                    Log.e("dataasd",it.item.data.toString())
                    viewBinding.resume.setText(it.item.data.toString())
                    resumename=it.item.data.toString()
                   // viewBinding.resume.setText(it.item.data.trim().toString())


                }
                is DataState.Error -> {

                   // Toast.makeText(this,"error",Toast.LENGTH_LONG).show()
                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

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



    private var postsObserverapplyjob: Observer<DataState<Jobapplicationresponse>> =
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
                    startActivity(Intent(this@Applyjobform, Jobpost::class.java))
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




    private var postsObserver: Observer<DataState<Jobquestionmodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                    Log.e("dataasd",it.item.data.toString())
                    validatePostsData(it.item)
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

    private fun validatePostsData(response: Jobquestionmodel) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.data != null && response.data.isNotEmpty()) {
                viewBinding.errorLayout.root.hide()
                viewBinding.recyclerView.show()
                jobQuestionAdaptor.addList(response.data)
            } else {
                jobQuestionAdaptor.addList(Collections.emptyList())

                viewBinding.errorLayout.errorText.text = "No Data Found"
                viewBinding.errorLayout.root.show()
                viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
            }


        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }


}