package technology.innovate.haziremployee.ui.applyjobform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import technology.innovate.haziremployee.rest.entity.resumeuploadmodel.ResumeuploadModel
import technology.innovate.haziremployee.utility.DataState

import javax.inject.Inject



@HiltViewModel
class UploaddocViewModel @Inject constructor(private val repository: JobquestionRepository) :
    ViewModel() {


    private val _notifications = MutableLiveData<DataState<ResumeuploadModel>>()
    val notifications: LiveData<DataState<ResumeuploadModel>> = _notifications

    fun uploadresume(file_name: MultipartBody.Part?) {
        viewModelScope.launch {
            repository.uploadresume(file_name)
                .onEach { dataState ->
                    dataState.let {
                        _notifications.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }



}