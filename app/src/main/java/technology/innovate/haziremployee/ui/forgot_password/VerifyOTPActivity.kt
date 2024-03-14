package technology.innovate.haziremployee.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.component_otp_view.view.*
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.callbacks.OtpVerifyCallBack
import technology.innovate.haziremployee.config.ApplicationConstants.PIN_TYPE_CONFIRM
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityVerifyOtpBinding
import technology.innovate.haziremployee.rest.entity.ApiResponse
import technology.innovate.haziremployee.rest.entity.ForgotPassword
import technology.innovate.haziremployee.rest.entity.VerifyOTP
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog


@AndroidEntryPoint
class VerifyOTPActivity : AppCompatActivity(), OtpVerifyCallBack {
    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var viewBinding: ActivityVerifyOtpBinding
    private var otp: String? = null
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(false)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp)
        viewBinding.viewModel = viewModel
        email = intent.getStringExtra("email")!!

        val otpVerifyCallBack: OtpVerifyCallBack = this
        viewBinding.pinView.setContext(otpVerifyCallBack, PIN_TYPE_CONFIRM)

        viewBinding.submitMaterialButton.setOnClickListener {
            viewModel.validateOtp(VerifyOTP(email, otp))
            viewModel.validateOtp.observe(this, validateOtpObserver)
        }

        viewBinding.backImageView.setOnClickListener {
            finish()
        }
        viewBinding.resendOtpTextView.setOnClickListener {
            viewBinding.pinView.resetFields()
            viewModel.resendOtp(ForgotPassword(email))
            viewModel.resendOtp.observe(this, resendOtpObserver)

        }

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }
        viewBinding.pinView.pinHiddenEditText.showKeyboard()
    }


    override fun createPinCallback(otp: String?) {
    }

    override fun confirmPinCallback(otp: String?) {
        viewBinding.submitMaterialButton.enable()
        this@VerifyOTPActivity.otp = otp
        viewModel.validateOtp(VerifyOTP(email, otp))
        viewModel.validateOtp.observe(this@VerifyOTPActivity, validateOtpObserver)
    }

    override fun onAllDigitNotCompleted() {
        viewBinding.submitMaterialButton.disable()
    }

    private var validateOtpObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateOtpResponse(it.item)
                }
                is DataState.Error -> {
                    viewBinding.pinView.resetFields()
                    dismissProgress()
                    showToast(it.error.toString())
                    startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                        putExtra("email", email)
                    })
                    finish()
                }
                is DataState.TokenExpired -> {
                    dismissProgress()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ), object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            finishAffinity()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                        }
                    })
                }
            }
        }

    private fun validateOtpResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
            startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                putExtra("email", email)
            })
            finish()
        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }
    }

    private var resendOtpObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateResendOtpResponse(it.item)
                }
                is DataState.Error -> {
                    dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    dismissProgress()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ), object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            finishAffinity()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                        }
                    })
                }
            }
        }

    private fun validateResendOtpResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }
}