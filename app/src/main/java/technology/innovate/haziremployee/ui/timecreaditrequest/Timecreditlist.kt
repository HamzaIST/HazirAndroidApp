package technology.innovate.haziremployee.ui.timecreaditrequest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityTimecreditlistBinding
import technology.innovate.haziremployee.rest.entity.deletecreditrequest.Deletecreditrequest
import technology.innovate.haziremployee.rest.entity.timecreditrequestlist.TimecreaditrequestlistModel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.util.*

//class Timecreditlist : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_timecreditlist)
//    }
//}
@AndroidEntryPoint
class Timecreditlist : AppCompatActivity(), Creditlistclick {
    private val viewModel: TimecreditViewmodel by viewModels()
    private lateinit var viewBinding: ActivityTimecreditlistBinding
    private lateinit var attendanceReportAdapter: TimeCreditlistAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_timecreditlist)
        setAttendanceReportAdapter()

        setUpListeners()

        getAttendanceReportFromRemote()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Timecreditlist, HomeActivity::class.java))
                finish()
            }
        })

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }


        viewBinding.submitMaterialButton.setOnClickListener {
            startActivity(Intent(this@Timecreditlist, Timecreditapply::class.java))
            finish()
        }
    }


    private fun setAttendanceReportAdapter() {
        attendanceReportAdapter = TimeCreditlistAdaptor(this@Timecreditlist, this,listOf())
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this@Timecreditlist)
        viewBinding.recyclerView.adapter = attendanceReportAdapter

    }




    private fun getAttendanceReportFromRemote() {
        viewModel.timecreditlist()
        viewModel.posts.observe(this, attendanceReportsObserver)
    }

    private var attendanceReportsObserver: Observer<DataState<TimecreaditrequestlistModel>> =
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


    private fun validateAttendanceReportData(response: TimecreaditrequestlistModel) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response?.data != null && response.data.isNotEmpty()) {
                viewBinding.errorLayout.root.hide()
                viewBinding.recyclerView.show()
                attendanceReportAdapter.addList(response.data)
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
            startActivity(Intent(this@Timecreditlist, HomeActivity::class.java))
            finish()
        }






    }


    fun logoutUser() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finishAffinity()
        showToast("Logged out Successfully")
    }

    override fun onDeleteClicked(id: Int) {
        CustomDialog(this).showDecisionButtonDialog(
            "Do you want to delete the request?",
            "Yes",
            "No",
            true,
            object : CustomDialog.onUserActionCLickListener {
                override fun negativeButtonClicked() {

                }

                override fun positiveButtonClicked() {

                    viewModel.deleteTimeCreditRequest(id.toString())
                    viewModel.deleteLeave.observe(this@Timecreditlist, deleteLeaveRequestObserver)

                }

            })

    }

    private var deleteLeaveRequestObserver: Observer<DataState<Deletecreditrequest>> =
        androidx.lifecycle.Observer<DataState<Deletecreditrequest>> {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateDeleteRequestResponse(it.item)
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

    private fun validateDeleteRequestResponse(response: Deletecreditrequest) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
            getAttendanceReportFromRemote()
        } else {
            CustomDialog(this@Timecreditlist).showInformationDialog(response.message)
        }

    }

}