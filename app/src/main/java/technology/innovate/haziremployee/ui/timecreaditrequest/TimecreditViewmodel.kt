package technology.innovate.haziremployee.ui.timecreaditrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.applycreditrequest.Applycreditresponsemodel
import technology.innovate.haziremployee.rest.entity.applycreditrequest.Newcreditrequestmodel
import technology.innovate.haziremployee.rest.entity.creditlistdetail.CreditlistdetailModel
import technology.innovate.haziremployee.rest.entity.deletecreditrequest.Deletecreditrequest
import technology.innovate.haziremployee.rest.entity.timecreditrequestlist.TimecreaditrequestlistModel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.Event
import javax.inject.Inject

//class TimecreditViewmodel {
//}

@HiltViewModel
class TimecreditViewmodel @Inject constructor(private val repository: TimecreditRepository) :
    ViewModel() {
    private val _posts = MutableLiveData<DataState<TimecreaditrequestlistModel>>()
    val posts: LiveData<DataState<TimecreaditrequestlistModel>> = _posts

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _deleteLeave = MutableLiveData<DataState<Deletecreditrequest>>()
    val deleteLeave: LiveData<DataState<Deletecreditrequest>> = _deleteLeave

    private val _timecreditdetail = MutableLiveData<DataState<CreditlistdetailModel>>()
    val timecreditdetail: LiveData<DataState<CreditlistdetailModel>> = _timecreditdetail



    private val _addnew= MutableLiveData<DataState<Applycreditresponsemodel>>()
    val addnew: LiveData<DataState<Applycreditresponsemodel>> = _addnew



    fun timecreditlist() {
        viewModelScope.launch {
            repository.timecreditlist()
                .onEach { dataState ->
                    dataState.let {
                        _posts.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun deleteTimeCreditRequest(id: String) {
        if (id == null) {
            _statusMessage.value = Event("Invalid id")
        } else {
            viewModelScope.launch {
                repository.deleteLeaveRequest(id).onEach { dataState ->
                    _deleteLeave.value = dataState
                }
                    .launchIn(viewModelScope)
            }
        }

    }
    fun timecreditdetail(id:String) {
        viewModelScope.launch {
            repository.timecreditlistdetail(id)
                .onEach { dataState ->
                    dataState.let {
                        _timecreditdetail.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun addnewrequest(newjobpostrequestmodel: Newcreditrequestmodel) {
        viewModelScope.launch {
            repository.applycreditrequest(newjobpostrequestmodel)
                .onEach { dataState ->
                    dataState.let {
                        _addnew.value = (it)
                    }
                }.launchIn(viewModelScope)
        }
    }




}