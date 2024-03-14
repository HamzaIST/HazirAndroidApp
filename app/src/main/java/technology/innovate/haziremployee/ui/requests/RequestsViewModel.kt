package technology.innovate.haziremployee.ui.requests

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.Event
import javax.inject.Inject


@HiltViewModel
class RequestsViewModel @Inject constructor(private val repository: RequestsRepository) :
    ViewModel() {

    private val _leaveTypes = MutableLiveData<DataState<LeaveTypes>>()
    val leaveTypes: LiveData<DataState<LeaveTypes>> = _leaveTypes

    var selectedLeaveType: MutableLiveData<LeaveTypeData?>? = MutableLiveData()

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    var selectedFileLiveData: MutableLiveData<String?>? = MutableLiveData()

    private val _applyLeave = MutableLiveData<DataState<ApiResponse2>>()
    val applyLeave: LiveData<DataState<ApiResponse2>> = _applyLeave

    private val _documentRequestTypes = MutableLiveData<DataState<RequestType>>()
    val documentRequestTypes: LiveData<DataState<RequestType>> = _documentRequestTypes

    var selectedDocumentRequestType: MutableLiveData<RequestTypeItem?>? = MutableLiveData()

    private val _requestDocument = MutableLiveData<DataState<ApiResponse>>()
    val requestDocument: LiveData<DataState<ApiResponse>> = _requestDocument

    private val _employeeRequests = MutableLiveData<DataState<EmployeeRequests>>()
    val employeeRequests: LiveData<DataState<EmployeeRequests>> = _employeeRequests

    private val _updateLeave = MutableLiveData<DataState<ApiResponse2>>()
    val updateLeave: LiveData<DataState<ApiResponse2>> = _updateLeave

    private val _deleteLeave = MutableLiveData<DataState<ApiResponse>>()
    val deleteLeave: LiveData<DataState<ApiResponse>> = _deleteLeave

    private val _updateDocument = MutableLiveData<DataState<ApiResponse>>()
    val updateDocument: LiveData<DataState<ApiResponse>> = _updateDocument

    private val _deleteDocumentRequest = MutableLiveData<DataState<ApiResponse>>()
    val deleteDocumentRequest: LiveData<DataState<ApiResponse>> = _deleteDocumentRequest

    fun getLeaveTypes() {
        viewModelScope.launch {
            repository.leaveTypes()
                .onEach { dataState ->
                    dataState.let {
                        _leaveTypes.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun applyLeave(request: ApplyLeave) {

        if (request.leave_type_id == null) {
            _statusMessage.value = Event("Select Leave type")
        } else if (TextUtils.isEmpty(request.description)) {
            _statusMessage.value = Event("Add Description")
        } else if (TextUtils.isEmpty(request.from_date)) {
            _statusMessage.value = Event("Start date is not selected")
        } else if (TextUtils.isEmpty(request.to_date)) {
            _statusMessage.value = Event("End date is not selected")
        } else {
            viewModelScope.launch {
                repository.applyLeave(request = request, selectedFileLiveData?.value)
                    .onEach { dataState ->
                        _applyLeave.value = dataState
                    }
                    .launchIn(viewModelScope)
            }
        }

    }

    fun getRequestTypes() {
        viewModelScope.launch {
            repository.requestTypes()
                .onEach { dataState ->
                    dataState.let {
                        _documentRequestTypes.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun requestDocument(request: DocumentRequest) {
        if (request.request_type == null) {
            _statusMessage.value = Event("Select Request for")
        } else if (TextUtils.isEmpty(request.email)) {
            _statusMessage.value = Event("Enter valid email")
        } else {
            viewModelScope.launch {
                repository.requestDocument(request = request, selectedFileLiveData?.value)
                    .onEach { dataState ->
                        _requestDocument.value = dataState
                    }
                    .launchIn(viewModelScope)
            }
        }

    }

    fun employeeRequests(request: GetRequests) {
        viewModelScope.launch {
            repository.employeeRequests(request)
                .onEach { dataState ->
                    dataState.let {
                        _employeeRequests.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun updateLeave(request: UpdateLeave) {
        if (request.leave_type_id == null) {
            _statusMessage.value = Event("Select Leave type")
        } else if (TextUtils.isEmpty(request.description)) {
            _statusMessage.value = Event("Add Description")
        } else if (TextUtils.isEmpty(request.from_date)) {
            _statusMessage.value = Event("Start date is not selected")
        } else if (TextUtils.isEmpty(request.to_date)) {
            _statusMessage.value = Event("End date is not selected")
        } else {
            viewModelScope.launch {
                repository.updateLeave(request = request, selectedFileLiveData?.value)
                    .onEach { dataState ->
                        _updateLeave.value = dataState
                    }
                    .launchIn(viewModelScope)
            }
        }
    }

    fun deleteLeaveRequest(request: DeleteReq) {
        if (request.request_id == null) {
            _statusMessage.value = Event("Invalid id")
        } else {
            viewModelScope.launch {
                repository.deleteLeaveRequest(request = request).onEach { dataState ->
                    _deleteLeave.value = dataState
                }
                    .launchIn(viewModelScope)
            }
        }

    }

    fun updateDocument(request: UpdateDocumentRequest) {
        if (request.request_type == null) {
            _statusMessage.value = Event("Select Request for")
        } else {
            viewModelScope.launch {
                repository.updateDocument(request = request, selectedFileLiveData?.value)
                    .onEach { dataState ->
                        _updateDocument.value = dataState
                    }
                    .launchIn(viewModelScope)
            }
        }

    }

    fun deleteDocumentRequest(request: DeleteReq) {
        if (request.request_id == null) {
            _statusMessage.value = Event("Invalid id")
        } else {
            viewModelScope.launch {
                repository.deleteDocumentRequest(request = request).onEach { dataState ->
                    _deleteDocumentRequest.value = dataState
                }
                    .launchIn(viewModelScope)
            }
        }

    }

    fun clearAttachment() {
        selectedFileLiveData?.value = null
    }


}