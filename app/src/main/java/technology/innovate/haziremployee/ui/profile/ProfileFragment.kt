package technology.innovate.haziremployee.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.FragmentProfileBinding
import technology.innovate.haziremployee.rest.entity.GetLeave
import technology.innovate.haziremployee.rest.entity.LeaveDataItem
import technology.innovate.haziremployee.rest.entity.Profile
import technology.innovate.haziremployee.rest.entity.employeecreditbalance.Employeecreditbalancemodel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.attendance.AttendanceActivity
import technology.innovate.haziremployee.ui.change_password.ChangePasswordActivity
import technology.innovate.haziremployee.ui.interviewround.Interviewroundlist
import technology.innovate.haziremployee.ui.jobpost.Jobpost
import technology.innovate.haziremployee.ui.leavebalance.LeaveBalance
import technology.innovate.haziremployee.ui.manager.Managerjobpostlist
import technology.innovate.haziremployee.ui.payslip.PayslipActivity
import technology.innovate.haziremployee.ui.personal_info.PersonalInfoActivity
import technology.innovate.haziremployee.ui.settings.SettingsActivity
import technology.innovate.haziremployee.ui.timecreaditrequest.Timecreditlist
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var leaveAdapter: LeaveAdapter
    private lateinit var viewBinding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    private var profileObserver: Observer<DataState<Profile>> =
        androidx.lifecycle.Observer<DataState<Profile>> {
            when (it)
            {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateProfileResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }


    private var employeecreditbalanceleavesObserver: Observer<DataState<Employeecreditbalancemodel>> =
        androidx.lifecycle.Observer<DataState<Employeecreditbalancemodel>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    try {
                        if (it.item.data.balance.toString().isNullOrEmpty())
                        {
                            viewBinding.employeecreditbalance.text="0"+"/360"
                        }
                        else{
                            viewBinding.employeecreditbalance.text=it.item.data.balance.toString()+"/360"
                        }

                    }
                    catch (ex:Exception)
                    {
                        viewBinding.employeecreditbalance.text="0"+"/360"

                    }
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                }
            }
        }

    private var leavesObserver: Observer<DataState<GetLeave>> =
            androidx.lifecycle.Observer<DataState<GetLeave>> {
            when (it) {

                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    try {
                        if(it.item.data?.leaveData!!.isNotEmpty())
                        {
                            for (i in 0..it.item.data.leaveData.size)
                            {
                                if (it.item.data.leaveData[0]!!.totalLeaves!!.isNotEmpty())
                                {
                                    viewBinding.firstleavedatalayyout.visibility=View.VISIBLE
                                    viewBinding.leaveLeftHeadingfirst.text = it.item.data.leaveData[0]?.leaveCode +" Left"
                                    viewBinding.leaveLeftfirst.text = it.item.data.leaveData[0]?.remainingLeaves.toString()
                                    viewBinding.leaveTotalfirst.text = "/"+it.item.data.leaveData[0]?.totalLeaves.toString()
                                }
                                if (it.item.data.leaveData[1]!!.totalLeaves!!.isNotEmpty())
                                {
                                    viewBinding.firstleavedatalayyout.visibility=View.VISIBLE
                                    viewBinding.secondleavelayout.visibility=View.VISIBLE
                                    viewBinding.leaveLeftHeading.text = it.item.data.leaveData[1]?.leaveCode +" Left"
                                    viewBinding.leaveLeft.text = it.item.data.leaveData[1]?.remainingLeaves.toString()
                                    viewBinding.leaveTotal.text = "/"+it.item.data.leaveData[1]?.totalLeaves.toString()
                                }
                            }
                        }

                        if (it.item.data.leaveData.size==2)
                        {
                            viewBinding.employeecreditbalancemainlayout.weightSum=2f
                        }
                        if (it.item.data.leaveData.size>2)
                        {
                            viewBinding.morelayout.visibility=View.VISIBLE
                        }
                        else
                        {
                            viewBinding.morelayout.visibility=View.GONE
                        }

                    }
                    catch (e:Exception)
                    {
                        e.printStackTrace()
                    }

                    viewBinding.morelayout.setOnClickListener {
                        startActivity(Intent(activity, LeaveBalance::class.java))
                    }
                   validateLeaveResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                }
            }
        }

    private fun validateProfileResponse(body: Profile) {
        if (activity != null && isAdded) {
            if (body.status == Constants.API_RESPONSE_CODE.OK) {
                if (body.profileData?.photo != "http://hazir-hr.ae/api/public/images/employee") {
                    loadLogo(body.profileData?.photo)
                } else {
                    loadLogo(SessionManager.user?.organisationLogo)
                }
                viewBinding.nameText.text = body.profileData?.name.toString()
                viewBinding.positionTv.text = body.profileData?.designation?.title.toString()


            } else {
                CustomDialog(requireActivity()).showInformationDialog(body.message)
            }

        }
    }

    private fun loadLogo(imageUrl: String?) {
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)

            .error(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(viewBinding.profilePicView)
    }

    private fun validateLeaveResponse(body: GetLeave) {
        if (activity != null && isAdded) {
            if (body.status == Constants.API_RESPONSE_CODE.OK) {
                viewBinding.availableLeave.text = body.data?.sumAvailableLeave.toString()
                viewBinding.totalLeave.text = "/" + body.data?.sumTotalLeave.toString()
                viewBinding.leaveRequested.text = body.data?.leaveRequested.toString()
                leaveAdapter.addList(body.data?.leaveData as ArrayList<LeaveDataItem>)

            }
            else
            {
                CustomDialog(requireActivity()).showInformationDialog(body.message)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        SessionManager.init(requireContext())


        if (SessionManager.managertype.toString()=="1")
        {
            viewBinding.managerjobpost.visibility=View.VISIBLE
            viewBinding.interviewroundlist.visibility=View.VISIBLE
        }
        else
        {
            viewBinding.managerjobpost.visibility=View.GONE
            viewBinding.interviewroundlist.visibility=View.GONE
        }

//        if (SessionManager.username.toString()=="24" || SessionManager.username.toString()=="19")
//        {
//            viewBinding.CreditRequest.visibility=View.VISIBLE
//        }
//        else{
//            viewBinding.CreditRequest.visibility=View.GONE
//        }

            if (SessionManager.username.toString().equals("106"))
            {
                viewBinding.creditbalancelayout.visibility=View.VISIBLE
            }

        Log.e("managertype",SessionManager.managertype.toString())
        initLeaveRecyclerAdapter()
        getProfileFromRemote()
        getLeavesFromRemote()
        setUpListeners()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SessionManager.user?.photo != "http://hazir-hr.ae/api/public/images/employee") {
            loadLogo(SessionManager.user?.photo)
        } else {
            loadLogo(SessionManager.user?.organisationLogo)
        }
    }

    private fun initLeaveRecyclerAdapter() {
        leaveAdapter = LeaveAdapter(requireContext())
        viewBinding.leaveRv.itemAnimator = DefaultItemAnimator()
        viewBinding.leaveRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.leaveRv.adapter = leaveAdapter
    }

    private fun getProfileFromRemote() {
        viewModel.profile()
        viewModel.profile.observe(viewLifecycleOwner, profileObserver)
    }

    private fun getLeavesFromRemote() {
        viewModel.leaves()
        viewModel.leaves.observe(viewLifecycleOwner, leavesObserver)

        if (SessionManager.username.toString().equals("106"))
        {
            viewModel.employeecreditbalance()
         viewModel.employeecreditbalance.observe(viewLifecycleOwner, employeecreditbalanceleavesObserver)

        }

//        viewModel.employeecreditbalance()
//        viewModel.employeecreditbalance.observe(viewLifecycleOwner, employeecreditbalanceleavesObserver)

    }

    private fun setUpListeners() {
        //
        viewBinding.interviewroundlist.setOnClickListener {
            startActivity(Intent(activity, Interviewroundlist::class.java))
        }
        viewBinding.jobpost.setOnClickListener {
            startActivity(Intent(activity, Jobpost::class.java))
        }
        viewBinding.managerjobpost.setOnClickListener {
            startActivity(Intent(activity, Managerjobpostlist::class.java))
        }

        viewBinding.CreditRequest.setOnClickListener {
            startActivity(Intent(activity, Timecreditlist::class.java))

        }
        viewBinding.morelayout.setOnClickListener {
            startActivity(Intent(activity, LeaveBalance::class.java))
        }

        ////
        viewBinding.personalInfo.setOnClickListener {
            startActivity(Intent(activity, PersonalInfoActivity::class.java))
        }

        viewBinding.payslip.setOnClickListener {
            startActivity(Intent(activity, PayslipActivity::class.java))
        }

        viewBinding.changePass.setOnClickListener {
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
        }

        viewBinding.attendanceLog.setOnClickListener {
            startActivity(Intent(activity, AttendanceActivity::class.java))
        }

        viewBinding.settings.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }


        viewBinding.leavebalancelayout.setOnClickListener {
            startActivity(Intent(activity, LeaveBalance::class.java))
        }

        viewBinding.logOut.setOnClickListener {
            val message: String = if (SessionManager.isTimerRunning == true) {
                "Timer will get cleared. Do you want to logout from Hazir?"
            } else {
                "Do you want to logout from Hazir?"
            }
            CustomDialog(requireActivity()).showDecisionButtonDialog(
                message,
                "Yes",
                "No",
                true,
                object : CustomDialog.onUserActionCLickListener {
                    override fun negativeButtonClicked() {

                    }

                    override fun positiveButtonClicked() {
                        (activity as? HomeActivity?)?.logoutUser()
                    }

                })
        }

    }


}
