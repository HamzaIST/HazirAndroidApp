package technology.innovate.haziremployee.ui.payslip

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.ReportRequest
import technology.innovate.haziremployee.rest.entity.paysliplistmodel.Paysliplistmodel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.Event
import javax.inject.Inject

//class PayslipViewModel {
//}
@HiltViewModel
class PayslipViewModel @Inject constructor(private val repository: PayslipRepository) :
    ViewModel() {
    private val _posts = MutableLiveData<DataState<Paysliplistmodel>>()
    val posts: LiveData<DataState<Paysliplistmodel>> = _posts

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    fun payslipReports(request: ReportRequest) {
        when {
            TextUtils.isEmpty(request.from_date) -> {
                _statusMessage.value = Event("From date is not selected")
            }
            TextUtils.isEmpty(request.from_date) -> {
                _statusMessage.value = Event("To date is not selected")
            }
            else -> {
                viewModelScope.launch {
                    repository.payslipReports(request = request)
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