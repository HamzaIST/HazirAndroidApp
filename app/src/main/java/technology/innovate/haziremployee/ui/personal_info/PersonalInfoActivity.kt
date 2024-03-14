package technology.innovate.haziremployee.ui.personal_info

import android.app.DatePickerDialog
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
import technology.innovate.haziremployee.databinding.ActivityPersonalInfoBinding
import technology.innovate.haziremployee.rest.entity.Profile
import technology.innovate.haziremployee.rest.entity.UpdateProfile
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.ui.profile.ProfileViewModel
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PersonalInfoActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var viewBinding: ActivityPersonalInfoBinding
    private var calendar = Calendar.getInstance()
    private val dateFormat = "dd-MM-yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(true)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info)
        setUpListeners()
        setUpObservers()
        getProfileFromRemote()

    }

    private fun setUpListeners() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewBinding.dateOfBirthEditText.setText(SimpleDateFormat(dateFormat, Locale.US).format(calendar.time))
            }
        val datePicker = DatePickerDialog(
            this@PersonalInfoActivity,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()

        viewBinding.dateOfBirthEditText.setOnClickListener {
            viewBinding.dateOfBirthEditText.hideKeyboard()
            datePicker.show()
        }

        viewBinding.backImageView.setOnClickListener {
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            finish()
        }

        viewBinding.saveMaterialButton.setOnClickListener {
            viewModel.updateProfile(UpdateProfile(viewBinding.nameTextInputEditText.text.toString(), viewBinding.dateOfBirthEditText.text.toString(), viewBinding.contactNumberTextInputEditText.text.toString()))
            viewModel.updateProfile.observe(this, updateProfileObserver)

        }
    }

    private fun setUpObservers(){
        viewModel.statusMessage.observe(this@PersonalInfoActivity) { it ->
            it.getContentIfNotHandled()?.let {
                if (it.contains("Enter valid name", true)) {
                    viewBinding.nameTextInputLayout.error = it
                    if (viewBinding.contactNumberTextInputLayout.isErrorEnabled) {
                        viewBinding.contactNumberTextInputLayout.isErrorEnabled = false
                    }
                    if (viewBinding.dateOfBirthTextInputLayout.isErrorEnabled) {
                        viewBinding.dateOfBirthTextInputLayout.isErrorEnabled = false
                    }
                } else if (it.contains("Enter valid contact number", true)) {
                    viewBinding.contactNumberTextInputLayout.error = it
                    if (viewBinding.nameTextInputLayout.isErrorEnabled) {
                        viewBinding.nameTextInputLayout.isErrorEnabled = false
                    }
                    if (viewBinding.dateOfBirthTextInputLayout.isErrorEnabled) {
                        viewBinding.dateOfBirthTextInputLayout.isErrorEnabled = false
                    }

                } else if (it.contains("Select your date of birth", true)) {
                    viewBinding.dateOfBirthTextInputLayout.error = it
                    if (viewBinding.nameTextInputLayout.isErrorEnabled) {
                        viewBinding.nameTextInputLayout.isErrorEnabled = false
                    }
                    if (viewBinding.contactNumberTextInputLayout.isErrorEnabled) {
                        viewBinding.contactNumberTextInputLayout.isErrorEnabled = false
                    }

                } else {
                    viewBinding.nameTextInputLayout.isErrorEnabled = false
                    viewBinding.dateOfBirthTextInputLayout.isErrorEnabled = false
                    viewBinding.contactNumberTextInputLayout.isErrorEnabled = false
                    showToast(it)
                }

            }
        }

    }

    private fun getProfileFromRemote() {
        viewModel.profile()
        viewModel.profile.observe(this, profileObserver)
    }


    private var profileObserver: Observer<DataState<Profile>> =
        androidx.lifecycle.Observer<DataState<Profile>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateProfileResponse(it.item)
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

    private var updateProfileObserver: Observer<DataState<Profile>> =
        androidx.lifecycle.Observer<DataState<Profile>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateUpdateProfileResponse(it.item)
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


    private fun validateUpdateProfileResponse(body: Profile) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(body.message)
            finish()
        } else {
            CustomDialog(this).showInformationDialog(body.message)
        }
    }

    private fun validateProfileResponse(body: Profile) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            if (body.profileData?.name != null) {
                viewBinding.nameTextInputEditText.setText(body.profileData.name.toString())
            }

            if (body.profileData?.dateOfBirth != null) {
                viewBinding.dateOfBirthEditText.setText(body.profileData.dateOfBirth.toString())
            }

            if (body.profileData?.personalContactNumber != null) {
                viewBinding.contactNumberTextInputEditText.setText(body.profileData.personalContactNumber.toString())
            }

        } else {
            CustomDialog(this).showInformationDialog(body.message)
        }
    }

    fun logoutUser() {
        SessionManager.deleteAllUserInfo()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
        showToast("Logged out Successfully")
    }

}