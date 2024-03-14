package technology.innovate.haziremployee.ui.forgot_password

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.Event
import technology.innovate.haziremployee.utils.Utils
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val forgotPasswordRepository: ForgotPasswordRepository) :
    ViewModel() {
    var userName: MutableLiveData<String> = MutableLiveData()

    var oldPassword: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    var confirmPassword: MutableLiveData<String> = MutableLiveData()

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _forgotPassword = MutableLiveData<DataState<ApiResponse>>()
    val forgotPassword: LiveData<DataState<ApiResponse>> = _forgotPassword

    private val _validateOtp = MutableLiveData<DataState<ApiResponse>>()
    val validateOtp: LiveData<DataState<ApiResponse>> = _validateOtp

    private val _resendOtp = MutableLiveData<DataState<ApiResponse>>()
    val resendOtp: LiveData<DataState<ApiResponse>> = _resendOtp

    private val _resetPassword = MutableLiveData<DataState<ApiResponse>>()
    val resetPassword: LiveData<DataState<ApiResponse>> = _resetPassword

    private val _changePassword = MutableLiveData<DataState<ChangePassword>>()
    val changePassword: LiveData<DataState<ChangePassword>> = _changePassword


    fun forgotPassword() {
        val request = ForgotPassword(userName.value)
        when {
            TextUtils.isEmpty(request.email) -> {
                _statusMessage.value = Event("Enter Valid Username")
            }
            else -> {
                viewModelScope.launch {
                    forgotPasswordRepository.forgotPassword(request = request).onEach { dataState ->
                        _forgotPassword.value = dataState
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    fun validateOtp(request: VerifyOTP) {
        when {
            TextUtils.isEmpty(request.email) -> {
                _statusMessage.value = Event("Invalid Username")
            }
            TextUtils.isEmpty(request.otp) -> {
                _statusMessage.value = Event("Invalid otp")
            }
            else -> {
                viewModelScope.launch {
                    forgotPasswordRepository.validateOtp(request = request).onEach { dataState ->
                        _validateOtp.value = dataState
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    fun resendOtp(request: ForgotPassword) {
        when {
            TextUtils.isEmpty(request.email) -> {
                _statusMessage.value = Event("Invalid Username")
            }
            else -> {
                viewModelScope.launch {
                    forgotPasswordRepository.resendOtp(request = request).onEach { dataState ->
                        _resendOtp.value = dataState
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    fun resetPassword(email: String) {
        if (TextUtils.isEmpty(email)){
            _statusMessage.value = Event("Invalid Username")
        }else if (TextUtils.isEmpty(password.value)){
            _statusMessage.value = Event("Enter New Password")
        }
        else if (TextUtils.isEmpty(confirmPassword.value)){
            _statusMessage.value = Event("Re-Enter New Password")
        }
        else if (password.value != confirmPassword.value){
            _statusMessage.value = Event("New Password and Re-Enter Password is not match")
        }else{
            viewModelScope.launch {
                val request = ResetPassword(email, Utils.md5(password.value), Utils.md5(confirmPassword.value))
                println("reset password input = ${Gson().toJson(request)}")
                forgotPasswordRepository.resetPassword(request = request).onEach { dataState ->
                    _resetPassword.value = dataState
                }
                    .launchIn(viewModelScope)
            }
        }

    }

    fun changePassword() {
        if (TextUtils.isEmpty(oldPassword.value)){
            _statusMessage.value = Event("Enter Old Password")
        }else if (TextUtils.isEmpty(password.value)){
            _statusMessage.value = Event("Enter New Password")
        } else if (TextUtils.isEmpty(confirmPassword.value)){
            _statusMessage.value = Event("Re-Enter New Password")
        }
        else if (password.value != confirmPassword.value){
            _statusMessage.value = Event("New Password and Re-Enter Password is not match")
        }else{
            viewModelScope.launch {
                val request = PasswordRequest(Utils.md5(oldPassword.value), Utils.md5(password.value), Utils.md5(confirmPassword.value))
                println("change password request = ${Gson().toJson(request)}")
                forgotPasswordRepository.changePassword(request = request).onEach { dataState ->
                    _changePassword.value = dataState
                }
                    .launchIn(viewModelScope)
            }
        }

    }

}