package technology.innovate.haziremployee.ui.change_password


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.ActivityChangePasswordBinding
import technology.innovate.haziremployee.rest.entity.ChangePassword
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.forgot_password.ForgotPasswordViewModel
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var viewBinding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(false)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        viewBinding.viewModel = viewModel

        viewModel.statusMessage.observe(this@ChangePasswordActivity) { it ->
            it.getContentIfNotHandled()?.let {
                if (it.contains("Enter Old Password", true)) {
                    viewBinding.oldPasswordTextInputLayout.error = it
                    if (viewBinding.passwordTextInputLayout.isErrorEnabled) {
                        viewBinding.passwordTextInputLayout.isErrorEnabled = false
                    }
                    if (viewBinding.confirmPasswordTextInputLayout.isErrorEnabled) {
                        viewBinding.confirmPasswordTextInputLayout.isErrorEnabled = false
                    }
                } else if (it.contains("Enter New Password", true)) {
                    viewBinding.passwordTextInputLayout.error = it
                    if (viewBinding.oldPasswordTextInputLayout.isErrorEnabled) {
                        viewBinding.oldPasswordTextInputLayout.isErrorEnabled = false
                    }
                    if (viewBinding.confirmPasswordTextInputLayout.isErrorEnabled) {
                        viewBinding.confirmPasswordTextInputLayout.isErrorEnabled = false
                    }

                } else if (it.contains("Re-Enter New Password", true)) {
                    viewBinding.confirmPasswordTextInputLayout.error = it
                    if (viewBinding.oldPasswordTextInputLayout.isErrorEnabled) {
                        viewBinding.oldPasswordTextInputLayout.isErrorEnabled = false
                    }
                    if (viewBinding.passwordTextInputLayout.isErrorEnabled) {
                        viewBinding.passwordTextInputLayout.isErrorEnabled = false
                    }

                } else {
                    viewBinding.oldPasswordTextInputLayout.isErrorEnabled = false
                    viewBinding.passwordTextInputLayout.isErrorEnabled = false
                    viewBinding.confirmPasswordTextInputLayout.isErrorEnabled = false
                    showToast(it)
                }

            }
        }

        viewBinding.backImageView.setOnClickListener {
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            finish()
        }

        viewBinding.oldPasswordTextInputEditText.showKeyboard()

        viewBinding.submitMaterialButton.setOnClickListener {
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            viewModel.changePassword()
            viewModel.changePassword.observe(this, changePasswordObserver)
        }
    }

    private var changePasswordObserver: Observer<DataState<ChangePassword>> =
        androidx.lifecycle.Observer<DataState<ChangePassword>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateChangePasswordResponse(it.item)
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

    private fun validateChangePasswordResponse(response: ChangePassword) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
            finishAffinity()
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }
    }
}