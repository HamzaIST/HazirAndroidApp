package technology.innovate.haziremployee.ui.forgot_password


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityForgotPasswordBinding
import technology.innovate.haziremployee.rest.entity.ApiResponse
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog


@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {
    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var viewBinding: ActivityForgotPasswordBinding

    private var forgotPasswordObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateForgotPasswordResponse(it.item)
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
                                finishAffinity()
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                            }
                        })
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(false)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        viewBinding.viewModel = viewModel
        viewBinding.submitMaterialButton.setOnClickListener {
            viewBinding.userNameTextInputEditText.hideKeyboard()
            viewModel.forgotPassword()
            viewModel.forgotPassword.observe(this, forgotPasswordObserver)
        }

        viewBinding.backImageView.setOnClickListener {
            viewBinding.userNameTextInputEditText.hideKeyboard()
            finish()
        }

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        viewBinding.userNameTextInputEditText.showKeyboard()


    }

    private fun validateForgotPasswordResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
            startActivity(Intent(this@ForgotPasswordActivity, VerifyOTPActivity::class.java).apply {
                putExtra("email", viewModel.userName.value)
            })

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

}


