package technology.innovate.haziremployee.ui.attendance

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.AttendenceReport
import technology.innovate.haziremployee.rest.entity.ReportRequest
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.Event
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository) :
    ViewModel() {
    private val _posts = MutableLiveData<DataState<AttendenceReport>>()
    val posts: LiveData<DataState<AttendenceReport>> = _posts

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    fun attendanceReports(request: ReportRequest) {
        when {
            TextUtils.isEmpty(request.from_date) -> {
                _statusMessage.value = Event("From date is not selected")
            }
            TextUtils.isEmpty(request.from_date) -> {
                _statusMessage.value = Event("To date is not selected")
            }
            else -> {
                viewModelScope.launch {
                    repository.attendanceReports(request = request)
                        .onEach { dataState ->
                            dataState.let {
                                _posts.value = it
                            }
                        }.launchIn(viewModelScope)
                }
            }
        }


    }

}