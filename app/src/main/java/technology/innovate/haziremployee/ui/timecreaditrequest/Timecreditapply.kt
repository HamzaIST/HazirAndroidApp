package technology.innovate.haziremployee.ui.timecreaditrequest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R

import technology.innovate.haziremployee.databinding.ActivityTimecreditapplyBinding
import technology.innovate.haziremployee.rest.entity.applycreditrequest.Applycreditresponsemodel
import technology.innovate.haziremployee.rest.entity.applycreditrequest.Newcreditrequestmodel
import technology.innovate.haziremployee.ui.HomeActivity

import technology.innovate.haziremployee.ui.attendance.AttendanceReportAdapter
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.hide
import technology.innovate.haziremployee.utility.show

import technology.innovate.haziremployee.utility.showToast
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class Timecreditapply : AppCompatActivity() {

    var isAllFieldsChecked = false
    private val viewModel: TimecreditViewmodel by viewModels()
    private lateinit var viewBinding: ActivityTimecreditapplyBinding
    private lateinit var attendanceReportAdapter: AttendanceReportAdapter
    var categoryarray: ArrayList<Int>? = null
    var categoryspinner:Int=5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_timecreditapply)


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Timecreditapply, Timecreditlist::class.java))
                finish()
            }
        })


        viewBinding.materialToolbar.setOnClickListener {
            startActivity(Intent(this@Timecreditapply, Timecreditlist::class.java))
            finish()
        }



        viewBinding.selectdate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewBinding.selectdate.text = sdf.format(datePicker.selection)

            }

        }

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        categoryarray= ArrayList()

        for (i in 5..60 step 5)
        {
            categoryarray!!.add((i))
        }

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, categoryarray!!)
        viewBinding.categoryspinner!!.adapter = adapter


        viewBinding.categoryspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

            Log.e("timeee",adapter.getItem(position).toString())
            categoryspinner= adapter.getItem(position)!!
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }


        viewBinding.submitMaterialButton.setOnClickListener {
            viewBinding.categoryerror.visibility=View.GONE
            isAllFieldsChecked = CheckAllFields()
            if(isAllFieldsChecked) {

                val newcreditrequestmodel=Newcreditrequestmodel(
                    usageType = "early_going",
                    reason = viewBinding.reason.text?.trim().toString(),
                    creditsRequiredInMins = categoryspinner,
                    date = viewBinding.selectdate.text.trim().toString()

                )

                viewModel.addnewrequest(newcreditrequestmodel)
                viewModel.addnew.observe(this, postsObserverapplyjob)
            }


        }



    }

    private var postsObserverapplyjob: Observer<DataState<Applycreditresponsemodel>> =
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
                    val intent=Intent(this, Timecreditlist::class.java)
                    startActivity(intent)
                    finish()


                }
                is DataState.Error -> {

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
    private  fun CheckAllFields(): Boolean {
        if (viewBinding.selectdate.length()==0)
        {
            viewBinding.selectdate.setError("This field is required");
            return false
        }
        if (viewBinding.categoryspinner.isEmpty())
        {
            viewBinding.categoryerror.visibility=View.VISIBLE
            return false
        }



        if (viewBinding.selectdate.length()==0)
        {
            viewBinding.selectdate.setError("This field is required");
            return false
        }

        if (viewBinding.reason.length()==0)
        {
            viewBinding.reason.setError("This field is required");
            return false
        }



        return true

    }









    private fun setFilterDefaultData() {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val date = Date()
        viewBinding.selectdate.text = sdf.format(date)

    }



    fun logoutUser() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finishAffinity()
        showToast("Logged out Successfully")
    }

}