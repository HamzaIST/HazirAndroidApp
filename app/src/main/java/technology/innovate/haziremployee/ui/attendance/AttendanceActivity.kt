package technology.innovate.haziremployee.ui.attendance

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityTimelogBinding
import technology.innovate.haziremployee.rest.entity.AttendenceReport
import technology.innovate.haziremployee.rest.entity.ReportRequest
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AttendanceActivity : AppCompatActivity() {

    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var viewBinding: ActivityTimelogBinding
    private lateinit var attendanceReportAdapter: AttendanceReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_timelog)
        setAttendanceReportAdapter()
        setFilterDefaultData()
        setUpListeners()
        getAttendanceReportFromRemote()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@AttendanceActivity, HomeActivity::class.java))
                finish()
            }
        })

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }
    }

    private fun setAttendanceReportAdapter() {
        attendanceReportAdapter = AttendanceReportAdapter(this@AttendanceActivity, listOf())
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this@AttendanceActivity)
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
         viewModel.attendanceReports(
            ReportRequest(
               viewBinding.fromDate.text.toString(),
                viewBinding.toDate.text.toString()
            )
        )
        viewModel.posts.observe(this, attendanceReportsObserver)
    }

    private var attendanceReportsObserver: Observer<DataState<AttendenceReport>> =
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


    private fun validateAttendanceReportData(response: AttendenceReport) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response.attendanceData?.data != null && response.attendanceData.data.isNotEmpty()) {
                viewBinding.errorLayout.root.hide()
                viewBinding.recyclerView.show()
                attendanceReportAdapter.addList(response.attendanceData.data)
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
            startActivity(Intent(this@AttendanceActivity, HomeActivity::class.java))
            finish()
        }


        viewBinding.fromDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewBinding.fromDate.text = sdf.format(datePicker.selection?.first)
                viewBinding.toDate.text = sdf.format(datePicker.selection?.second)
            }

        }
        viewBinding.toDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd"
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