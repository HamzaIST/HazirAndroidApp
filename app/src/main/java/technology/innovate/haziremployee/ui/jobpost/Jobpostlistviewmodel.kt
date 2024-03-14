package technology.innovate.haziremployee.ui.jobpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.branchlistmodel.Branchlistmodel
import technology.innovate.haziremployee.rest.entity.departmentlistmodel.Depalrtmentlistresponse
import technology.innovate.haziremployee.rest.entity.designationlist.Designationlistmodel
import technology.innovate.haziremployee.rest.entity.interviewroundlistmodel.Interviewroundlistmodel
import technology.innovate.haziremployee.rest.entity.jobcompanymodel.Jobcomapanymodel
import technology.innovate.haziremployee.rest.entity.jobpostlistresponse.DataXX
import technology.innovate.haziremployee.rest.entity.managerjobpostrequestlist.DataX
import technology.innovate.haziremployee.rest.entity.projectlistmodel.Projectlistmodel
import technology.innovate.haziremployee.ui.requests.RequestsRepository
import technology.innovate.haziremployee.utility.DataState
import javax.inject.Inject

//class Jobpostlistviewmodel {
//}
@HiltViewModel
class Jobpostlistviewmodel @Inject constructor(
    private val repository: RequestsRepository,

) :
    ViewModel() {


    private var _getjoblistResponse: Flow<PagingData<DataXX>>? = null
    private var _getmanagerjoblistResponse: Flow<PagingData<DataX>>? = null
    private var _getinterviewroundlistResponse: Flow<PagingData<technology.innovate.haziremployee.rest.entity.interviewroundlistmodel.DataX>>? = null

    private val _departmentRequestTypes = MutableLiveData<DataState<Depalrtmentlistresponse>>()
    val departmentRequestTypes: LiveData<DataState<Depalrtmentlistresponse>> = _departmentRequestTypes

    private val _getinterviewroundnewlistResponse = MutableLiveData<DataState<Interviewroundlistmodel>>()
    val getinterviewroundnewlistResponse: LiveData<DataState<Interviewroundlistmodel>> = _getinterviewroundnewlistResponse


    private val _designationRequestTypes = MutableLiveData<DataState<Designationlistmodel>>()
    val designationRequestTypes: LiveData<DataState<Designationlistmodel>> = _designationRequestTypes

    private val _branchlistRequestTypes = MutableLiveData<DataState<Branchlistmodel>>()
    val branchlistRequestTypes: LiveData<DataState<Branchlistmodel>> = _branchlistRequestTypes

    private val _companylistRequestTypes = MutableLiveData<DataState<Jobcomapanymodel>>()
    val companylistRequestTypes: LiveData<DataState<Jobcomapanymodel>> = _companylistRequestTypes

    private val _projectlistResponse= MutableLiveData<DataState<Projectlistmodel>>()
    val projectlistResponse: LiveData<DataState<Projectlistmodel>> = _projectlistResponse




    fun getjobList(orgid:String, departmentid:Int?, designationid:Int?,categoryid:Int?,status:Int?): Flow<PagingData<DataXX>> {
        _getjoblistResponse =  repository.joblist(orgid = orgid,departmentid, designationid, categoryid, status)
//        if (_getjoblistResponse !=null) return _getjoblistResponse as Flow<PagingData<DataXX>>
//        else {
//            _getjoblistResponse =  repository.joblist(orgid = orgid,departmentid, designationid, categoryid, status)
//        }
        return _getjoblistResponse as Flow<PagingData<DataXX>>
    }


    fun getmanagerjobList(departmentid:Int?, designationid:Int?,categoryid:Int?,status:Int?): Flow<PagingData<DataX>> {

        _getmanagerjoblistResponse =  repository.managerjoblist(departmentid, designationid, categoryid, status)

//        if (_getmanagerjoblistResponse !=null) return _getmanagerjoblistResponse as Flow<PagingData<DataX>>
//        else {
//            _getmanagerjoblistResponse =  repository.managerjoblist(departmentid, designationid, categoryid, status)
//        }
        return _getmanagerjoblistResponse as Flow<PagingData<DataX>>
    }



    fun departmentlist(id:String) {
        viewModelScope.launch {
            repository.departmentlist(id)
                .onEach { dataState ->
                    dataState.let {
                        _departmentRequestTypes.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun designationlist(id:String) {
        viewModelScope.launch {
            repository.designationlist(id)
                .onEach { dataState ->
                    dataState.let {
                        _designationRequestTypes.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun branchlist() {
        viewModelScope.launch {
            repository.branchlist()
                .onEach { dataState ->
                    dataState.let {
                        _branchlistRequestTypes.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun companylist() {
        viewModelScope.launch {
            repository.companylist()
                .onEach { dataState ->
                    dataState.let {
                        _companylistRequestTypes.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun getinterviewlist(jobapplicationid:Int?, interviewstatus:Int?,interviewdate:String?,interviewtype:Int?):
            Flow<PagingData<technology.innovate.haziremployee.rest.entity.interviewroundlistmodel.DataX>> {
        _getinterviewroundlistResponse =  repository.interviewlist(jobapplicationid, interviewstatus, interviewdate, interviewtype)
//        if (_getinterviewroundlistResponse !=null) return _getinterviewroundlistResponse as Flow<PagingData<technology.dubaileading.maccessemployee.rest.entity.interviewroundlistmodel.DataX>>
//        else {
//            _getinterviewroundlistResponse =  repository.interviewlist(jobapplicationid, interviewstatus, interviewdate, interviewtype)
//        }
        return _getinterviewroundlistResponse as Flow<PagingData<technology.innovate.haziremployee.rest.entity.interviewroundlistmodel.DataX>>
    }





    fun projectlist() {
        viewModelScope.launch {
            repository.projectlist()
                .onEach { dataState ->
                    dataState.let {
                        _projectlistResponse.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

}