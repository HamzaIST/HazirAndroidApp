package technology.innovate.haziremployee.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.badge.BadgeDrawable
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityHomeBinding
import technology.innovate.haziremployee.rest.entity.NotificationCount
import technology.innovate.haziremployee.rest.entity.checkMattendancePermission.checkMattendancePermissionModel
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var count: BadgeDrawable
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewBinding: ActivityHomeBinding

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadNotificationCountFromRemote()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        getDeviceInfo(this, Device.DEVICE_TYPE)?.let {
            if(it=="This is Tablet")
            {
                val displayMetrics = resources.displayMetrics
                val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
                val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
                val diagonalinch = Math.sqrt((xinch * xinch + yinch * yinch).toDouble())
                val screensize=diagonalinch.toString().replaceAfter(".","")

                if (screensize=="7.") {
                    viewBinding.homeImageView.layoutParams.width = 700
                    viewBinding.homeImageView.layoutParams.height = 480
                    val homeImageView =
                        viewBinding.homeImageView.layoutParams as ViewGroup.MarginLayoutParams
                    homeImageView.setMargins(0, 0, 0, 5)
                    viewBinding.homeImageView.layoutParams = homeImageView

                    val param =
                        viewBinding.homeImageButtonFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 0, 0, 50)
                    viewBinding.homeImageButtonFrameLayout.layoutParams = param
                }

                if (screensize=="8.") {
                    viewBinding.homeImageView.layoutParams.width = 700
                    viewBinding.homeImageView.layoutParams.height = 480
                    val homeImageView =
                        viewBinding.homeImageView.layoutParams as ViewGroup.MarginLayoutParams
                    homeImageView.setMargins(0, 0, 0, 5)
                    viewBinding.homeImageView.layoutParams = homeImageView


                    val param =
                        viewBinding.homeImageButtonFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 0, 0, 50)
                    viewBinding.homeImageButtonFrameLayout.layoutParams = param
                }

                else  if (screensize=="7.") {
                    viewBinding.homeImageView.layoutParams.width = 400
                    viewBinding.homeImageView.layoutParams.height = 300
                    val homeImageView =
                        viewBinding.homeImageView.layoutParams as ViewGroup.MarginLayoutParams
                    homeImageView.setMargins(0, 0, 0, 5)
                    viewBinding.homeImageView.layoutParams = homeImageView


                    val param =
                        viewBinding.homeImageButtonFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 0, 0, 50)
                    viewBinding.homeImageButtonFrameLayout.layoutParams = param
                }
                else
                {
                    viewBinding.homeImageView.layoutParams.width = 700
                    viewBinding.homeImageView.layoutParams.height = 480
                    val homeImageView = viewBinding.homeImageView.layoutParams as ViewGroup.MarginLayoutParams
                    homeImageView.setMargins(0, 0, 0, 5)
                    viewBinding.homeImageView.layoutParams = homeImageView
                    val param = viewBinding.homeImageButtonFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 0, 0, 50)
                    viewBinding.homeImageButtonFrameLayout.layoutParams = param
                }

            }

        }

        val intentFilter = IntentFilter("IntentFilterAction")
        LocalBroadcastManager.getInstance(this@HomeActivity)
            .registerReceiver(broadcastReceiver, intentFilter)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = host.navController
        viewBinding.bottomNavigationViewInclude.bottomNavigationView.selectedItemId = R.id.home

        setCountColor()
        setUpListeners()
        loadNotificationCountFromRemote()
        viewModel.checkMattendancePermission()
        viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)

    }


    private var loginObserver: Observer<DataState<checkMattendancePermissionModel>> =
        androidx.lifecycle.Observer<DataState<checkMattendancePermissionModel>> {
            when (it) {
                is DataState.Loading -> {
                    //showProgress()
                }
                is DataState.Success -> {
                   // dismissProgress()
                    if(it.item.data.mobileloginallowed==0)
                    {
                        val alertDialog = AlertDialog.Builder(this).create()
                        alertDialog.setMessage("You account has been disabled or deleted. Please contact Admin.")
                        alertDialog.setCancelable(false)

                        alertDialog.setButton(
                            AlertDialog.BUTTON_POSITIVE, "OK")
                        { dialog, which ->
                            SessionManager.isLoggedIn=false

                            (this as? HomeActivity?)?.logoutUser()
                            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                            finish()
                            alertDialog.cancel()
                        }
                        alertDialog.show()
                    }

                    SessionManager.attendenceaccess=it.item.data.mobileAttendanceAllowed.toString()
                    SessionManager.requestmoduleaccess=it.item.data.requestmoduleaccess.toString()
                    SessionManager.postupdate=it.item.data.isPostView.toString()

                }
                is DataState.Error -> {
                   // dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                }
            }
        }
    private fun setUpListeners() {
        viewBinding.bottomNavigationViewInclude.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.services -> {
//                    findNavController(R.id.fragmentContainerView).navigate(
//                        R.id.servicesFragment,
//                        null,
//                        getNavBuilder().build()
//                    )
                                        findNavController(R.id.fragmentContainerView).navigate(
                        R.id.attendanceFragment,
                        null,
                        getNavBuilder().build()
                    )
                         findNavController(R.id.fragmentContainerView).navigate(
                        R.id.attendanceFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.requests -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.requestsFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.home -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.homeFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.notifications -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.notificationsFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.profile -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.profileFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)

                }
            }
            return@setOnItemSelectedListener true

        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.label == "CheckInFragment" || destination.label == "CheckOutFragment") {
                viewBinding.bottomNavigationLinearLayout.hide()
                viewBinding.homeImageView.hide()
                viewBinding.homeImageButtonFrameLayout.hide()
            } else {
                viewBinding.bottomNavigationLinearLayout.show()
                viewBinding.homeImageView.show()
                viewBinding.homeImageButtonFrameLayout.show()
                if (destination.label == "HomeFragment") {
                    Log.e("1","HOme")
                    viewBinding.homeImageButtonFrameLayout.background =
                        ContextCompat.getDrawable(this, R.drawable.hom_fab)
                    viewBinding.homeImageButton.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity,R.drawable.home_select))
                    viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(2)?.isChecked =
                        true
                } else {
                    if (destination.label == "ServicesFragment") {
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(0)?.isChecked =
                            true
                    } else if (destination.label == "RequestsFragment") {
                        viewBinding.homeImageButton.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity,R.drawable.hazir_no))
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(1)?.isChecked =
                            true
                    } else if (destination.label == "NotificationsFragment") {
                        viewBinding.homeImageButton.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity,R.drawable.hazir_no))
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(3)?.isChecked =
                            true
                    } else if (destination.label == "ProfileFragment") {
                        viewBinding.homeImageButton.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity,R.drawable.hazir_no))
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(4)?.isChecked =
                            true
                    }
                    viewBinding.homeImageButtonFrameLayout.background =
                        ContextCompat.getDrawable(this, R.drawable.hom_fab_disable)
                }
            }
        }

        viewBinding.homeImageButton.setOnClickListener {
            viewBinding.bottomNavigationViewInclude.bottomNavigationView.selectedItemId = R.id.home
        }

    }

    private fun setCountColor() {
        count = viewBinding.bottomNavigationViewInclude.bottomNavigationView.getOrCreateBadge(R.id.notifications)
        count.backgroundColor = Color.RED
        count.badgeTextColor = Color.WHITE
    }

    fun loadNotificationCountFromRemote() {
        viewModel.notificationCount()
        viewModel.notificationCount.observe(this, notificationCountObserver)
    }

    private var notificationCountObserver: Observer<DataState<NotificationCount>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    validateNotificationCount(it.item)
                }
                is DataState.Error -> {
                }
                is DataState.TokenExpired -> {
                }
            }
        }
    fun getDevice5Inch(context: Context): Boolean {
        return try {
            val displayMetrics = context.resources.displayMetrics
            val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
            val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalinch = Math.sqrt((xinch * xinch + yinch * yinch).toDouble())
            Log.e("screen size",diagonalinch.toString())
            if (diagonalinch >= 7) {
                true
            } else {
                false
            }
        } catch (e: java.lang.Exception) {
            false
        }
    }


    open fun getDeviceInfo(context: Context?, device: Device): String? {
        try {
            if (device === Device.DEVICE_TYPE) {
                return if (isTablet(context!!)) {
                    if (getDevice5Inch(context)) {
                        "This is Tablet"
                    } else {
                        "This is Mobile"
                    }
                } else {
                    "This is Mobile"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
    private fun validateNotificationCount(body: NotificationCount) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            if (body.data != 0) {
                count.number = body.data!!
                count.isVisible = true
            } else {
                count.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@HomeActivity).unregisterReceiver(broadcastReceiver)
    }

    fun logoutUser() {
        SessionManager.deleteAllUserInfo()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
        showToast("Logged out Successfully")
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}
enum class Device {
    DEVICE_TYPE
}