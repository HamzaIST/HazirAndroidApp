package technology.innovate.haziremployee.ui.attendance

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
class AttendanceFragment : Fragment() {

    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var viewBinding: ActivityTimelogBinding
    private lateinit var attendanceReportAdapter: AttendanceReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ActivityTimelogBinding.inflate(inflater, container, false)
        setAttendanceReportAdapter()
        setFilterDefaultData()
        setUpListeners()
        getAttendanceReportFromRemote()

        viewModel.statusMessage.observe(requireActivity()) { it ->
            it.getContentIfNotHandled()?.let {
                requireActivity().showToast(it)
            }
        }
    
        return viewBinding.root
    }
    

    
    private fun setAttendanceReportAdapter() {
        attendanceReportAdapter = AttendanceReportAdapter(requireActivity(), listOf())
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
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
        viewModel.posts.observe(requireActivity(), attendanceReportsObserver)
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
                    requireActivity().showToast(it.error.toString())
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
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

    private fun setUpListeners() {
        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            //finish()
        }


        viewBinding.fromDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(childFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewBinding.fromDate.text = sdf.format(datePicker.selection?.first)
                viewBinding.toDate.text = sdf.format(datePicker.selection?.second)
            }

        }
        viewBinding.toDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(childFragmentManager, "DatePicker")
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
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finishAffinity()
        requireActivity().showToast("Logged out Successfully")
    }

}