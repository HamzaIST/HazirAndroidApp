package technology.innovate.haziremployee.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.component_otp_view.*
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityLoginBinding
import technology.innovate.haziremployee.rest.entity.ApiResponse
import technology.innovate.haziremployee.rest.entity.LoginRequest
import technology.innovate.haziremployee.rest.entity.LoginResponse
import technology.innovate.haziremployee.rest.entity.TokenRequest
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.forgot_password.ForgotPasswordActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import technology.innovate.haziremployee.utils.Utils


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var orgname = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        orgname=ArrayList()
        orgname.add("DLT")
        orgname.add("Dr. Sultan Al Qasimi Centre")


        viewBinding.submitMaterialButton.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                == PackageManager.PERMISSION_GRANTED) {
                for (i in  0..orgname.size-1)
                {
                    val loginRequest = LoginRequest(
                        device_token = getUniqueID(this@LoginActivity),
                        password = Utils.md5(
                            viewBinding.passwordTextInputEditText.text.toString().trim()
                        ),
                        username = viewBinding.userNameTextInputEditText.text.toString(),
                        organisation_name = orgname[i]
                    )

                    viewModel.userLogin(loginRequest)
                    viewModel.userDetails.observe(this, loginObserver)
                }

            }
            else
            {

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }



        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            Log.e("permissionlauncher",it.toString())
            if (it) {
//                val loginRequest = LoginRequest(
//                    device_token = getUniqueID(this@LoginActivity),
//                    password = Utils.md5(
//                        viewBinding.passwordTextInputEditText.text.toString().trim()
//                    ),
//                    username = viewBinding.userNameTextInputEditText.text.toString(),
//                    organisation_name = "DLT"
//                )
            }
            else {
                Toast.makeText(this,"Please grant Notification permission from App Settings",
                    Toast.LENGTH_LONG).show()
                //requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        viewModel.statusMessage.observe(this@LoginActivity) { it ->
            it.getContentIfNotHandled()?.let {
                if (it.contains("Username", true)) {
                    viewBinding.userNameTextInputLayout.error = it
                    if (viewBinding.passwordTextInputLayout.isErrorEnabled) {
                        viewBinding.passwordTextInputLayout.isErrorEnabled = false
                    }
                } else if (it.contains("Password", true)) {
                    viewBinding.passwordTextInputLayout.error = it
                    if (viewBinding.userNameTextInputLayout.isErrorEnabled) {
                        viewBinding.userNameTextInputLayout.isErrorEnabled = false
                    }

                } else {
                    viewBinding.userNameTextInputLayout.isErrorEnabled = false
                    viewBinding.passwordTextInputLayout.isErrorEnabled = false

                }
            }
        }

        viewBinding.forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

    }

    private var loginObserver: Observer<DataState<LoginResponse>> =
        androidx.lifecycle.Observer<DataState<LoginResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateLoginResponse(it.item)
                }
                is DataState.Error -> {
                    dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                }
            }
        }

    private var saveFcmTokenObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateSaveFcmTokenResponse(it.item)
                }
                is DataState.Error -> {
                    dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    dismissProgress()
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
    fun logoutUser() {
        SessionManager.deleteAllUserInfo()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
        showToast("Logged out Successfully")
    }
    private fun validateLoginResponse(response: LoginResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            SessionManager.user = response.data
            SessionManager.token = response.token
            SessionManager.encryptedorgid=response.data?.encryptorgid
            SessionManager.managertype=response.data?.manager!!
            SessionManager.profileid=response.data.profileid!!
            SessionManager.attendenceaccess=response.data.mobileattendanceallowed!!

            SessionManager.postupdate=response.data.ispostview.toString()
            SessionManager.username=response.data.organisationid.toString()
            SessionManager.loginname=response.data.name.toString().replaceAfter(" ","")

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //Log.e("error of firebase",task.result)
                    CustomDialog(this).showInformationDialog("Unable to get FCM token")
                }
                else
                {
                    Log.e("firebasetoken",task.result)
                    viewModel.saveFcmToken(TokenRequest(task.result, "android"))
                    viewModel.saveToken.observe(this, saveFcmTokenObserver)
                }
            })

         } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

    private fun validateSaveFcmTokenResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            SessionManager.isLoggedIn = true
            //startActivity(Intent(this@LoginActivity, SplashOrganisationActivity::class.java))
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
        else
         {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

    override fun onStart() {
        super.onStart()
        viewBinding.userNameTextInputEditText.showKeyboard()

    }
}