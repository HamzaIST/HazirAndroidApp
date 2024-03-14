package technology.innovate.haziremployee.ui.profile


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
import technology.innovate.haziremployee.rest.entity.GetLeave
import technology.innovate.haziremployee.rest.entity.Profile
import technology.innovate.haziremployee.rest.entity.UpdateProfile
import technology.innovate.haziremployee.rest.entity.employeecreditbalance.Employeecreditbalancemodel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.Event
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profile = MutableLiveData<DataState<Profile>>()
    val profile: LiveData<DataState<Profile>> = _profile

    private val _leaves = MutableLiveData<DataState<GetLeave>>()
    val leaves: LiveData<DataState<GetLeave>> = _leaves


    private val _employeecreditbalance = MutableLiveData<DataState<Employeecreditbalancemodel>>()
    val employeecreditbalance: LiveData<DataState<Employeecreditbalancemodel>> = _employeecreditbalance

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _updateProfile = MutableLiveData<DataState<Profile>>()
    val updateProfile: LiveData<DataState<Profile>> = _updateProfile

    fun profile() {
        viewModelScope.launch {
            profileRepository.getProfile()
                .onEach { dataState ->
                    dataState.let {
                        _profile.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun leaves() {
        viewModelScope.launch {
            profileRepository.getLeaves()
                .onEach { dataState ->
                    dataState.let {
                        _leaves.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }
    fun employeecreditbalance() {
        viewModelScope.launch {
            profileRepository.getEmployeecreditbalance()
                .onEach { dataState ->
                    dataState.let {
                        _employeecreditbalance.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun updateProfile(request: UpdateProfile) {
        if (TextUtils.isEmpty(request.employee_name)){
            _statusMessage.value = Event("Enter valid name")
        }else if (TextUtils.isEmpty(request.phone)){
            _statusMessage.value = Event("Enter valid contact number")
        }
        else if (TextUtils.isEmpty(request.date_of_birth)){
            _statusMessage.value = Event("Select your date of birth")
        } else{
            viewModelScope.launch {
                println("reset password input = ${Gson().toJson(request)}")
                profileRepository.updateProfile(request = request).onEach { dataState ->
                    _updateProfile.value = dataState
                }
                    .launchIn(viewModelScope)
            }
        }

    }

}