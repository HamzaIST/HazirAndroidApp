package technology.innovate.haziremployee.ui.leavebalance

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
import technology.innovate.haziremployee.databinding.ActivityLeaveBalanceBinding
import technology.innovate.haziremployee.rest.entity.GetLeave
import technology.innovate.haziremployee.rest.entity.LeaveDataItem
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.ui.profile.LeaveAdapter
import technology.innovate.haziremployee.ui.profile.ProfileViewModel
import technology.innovate.haziremployee.utility.*
import java.util.*

@AndroidEntryPoint
class LeaveBalance : AppCompatActivity() {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var viewBinding: ActivityLeaveBalanceBinding
    private lateinit var leaveAdapter: LeaveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_leave_balance)

        setUpListeners()

        viewModel.leaves()
        viewModel.leaves.observe(this, leavesObserver)
        initLeaveRecyclerAdapter()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@LeaveBalance, HomeActivity::class.java))
                finish()
            }
        })

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }
    }

    private fun initLeaveRecyclerAdapter() {
        leaveAdapter = LeaveAdapter(this)
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerView.adapter = leaveAdapter
    }


    private var leavesObserver: Observer<DataState<GetLeave>> =
        androidx.lifecycle.Observer<DataState<GetLeave>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateLeaveResponse(it.item)
                }
                is DataState.Error -> {
                    dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                }
            }
        }


    private fun validateLeaveResponse(body: GetLeave) {
        leaveAdapter.addList(body.data?.leaveData as ArrayList<LeaveDataItem>)
    }



    private fun setUpListeners() {
        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    fun logoutUser() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finishAffinity()
        showToast("Logged out Successfully")
    }

}