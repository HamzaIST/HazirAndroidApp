package technology.innovate.haziremployee.ui.payslip

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityPayslipBinding
import technology.innovate.haziremployee.rest.entity.ReportRequest
import technology.innovate.haziremployee.rest.entity.paysliplistmodel.Paysliplistmodel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.hide
import technology.innovate.haziremployee.utility.show
import technology.innovate.haziremployee.utility.showToast
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PayslipActivity : AppCompatActivity() {
    var PERMISSION_CODE = 101
    private val viewModel: PayslipViewModel by viewModels()
    private lateinit var viewBinding:ActivityPayslipBinding
    private lateinit var attendanceReportAdapter: PayslipAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_payslip)
        setAttendanceReportAdapter()
        setFilterDefaultData()
        setUpListeners()
        if (checkPermissions()) {
            // if permission is granted we are displaying a toast message.
           // Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            // if the permission is not granted
            // we are calling request permission method.
            requestPermission()
        }
        getAttendanceReportFromRemote()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@PayslipActivity, HomeActivity::class.java))
                finish()
            }
        })

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }
    }

    fun checkPermissions(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        // on below line we are returning true if both the
        // permissions are granted and returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    // on below line we are creating a function to request permission.
    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }
    private fun setAttendanceReportAdapter() {
        attendanceReportAdapter = PayslipAdaptor(this@PayslipActivity, listOf(),viewBinding.progressBar)
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this@PayslipActivity)
        viewBinding.recyclerView.adapter = attendanceReportAdapter

    }


    private fun setFilterDefaultData() {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val date = Date()
        viewBinding.fromDate.text = sdf.format(date)
        viewBinding.toDate.text = sdf.format(date)
    }

    private fun getAttendanceReportFromRemote() {
        viewModel.payslipReports(
            ReportRequest(
                viewBinding.fromDate.text.toString(),
                viewBinding.toDate.text.toString()
            )
        )
        viewModel.posts.observe(this, attendanceReportsObserver)
    }

    private var attendanceReportsObserver: Observer<DataState<Paysliplistmodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateAttendanceReportData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                logoutUser()
                            }
                        })
                }
            }
        }


    private fun validateAttendanceReportData(response: Paysliplistmodel) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response?.data != null && response.data.data.isNotEmpty()) {
                viewBinding.errorLayout.root.hide()
                viewBinding.recyclerView.show()
                attendanceReportAdapter.addList(response.data.data)
            } else {
                attendanceReportAdapter.addList(Collections.emptyList())

                viewBinding.errorLayout.errorText.text = "No Data Found"
                viewBinding.errorLayout.root.show()
                viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
            }


        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

    private fun setUpListeners() {
        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PayslipActivity, HomeActivity::class.java))
            finish()
        }


        viewBinding.fromDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "MMMM" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewBinding.fromDate.text = sdf.format(datePicker.selection?.first)
                viewBinding.toDate.text = sdf.format(datePicker.selection?.second)
            }

        }
        viewBinding.toDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "MMMM"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewBinding.fromDate.text = sdf.format(datePicker.selection?.first)
                viewBinding.toDate.text = sdf.format(datePicker.selection?.second)

            }
        }

        viewBinding.search.setOnClickListener {

            getAttendanceReportFromRemote()

        }

    }


    fun logoutUser() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finishAffinity()
        showToast("Logged out Successfully")
    }

}