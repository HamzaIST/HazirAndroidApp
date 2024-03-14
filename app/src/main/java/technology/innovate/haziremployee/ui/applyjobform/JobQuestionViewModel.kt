package technology.innovate.haziremployee.ui.applyjobform

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.innovate.haziremployee.rest.entity.addnewjobresponse.AddNewjobresponseModel
import technology.innovate.haziremployee.rest.entity.countrylistmodel.CountrylistModel
import technology.innovate.haziremployee.rest.entity.currencylistmodel.Currencylistmodel
import technology.innovate.haziremployee.rest.entity.interviewchangestatusmodel.Interviewchangestatusmodel
import technology.innovate.haziremployee.rest.entity.interviewchangestatusmodel.Interviewchangestatusrequest
import technology.innovate.haziremployee.rest.entity.interviewjobdetailmodel.Interviewjobdetailmodel
import technology.innovate.haziremployee.rest.entity.jobapplicationrequest.JobApplicationRequest
import technology.innovate.haziremployee.rest.entity.jobapplicationresponse.Jobapplicationresponse
import technology.innovate.haziremployee.rest.entity.jobquestions.Jobquestionmodel
import technology.innovate.haziremployee.rest.entity.managerjobdetailmodel.Mangerjobdetailmanagermodel
import technology.innovate.haziremployee.rest.entity.newjobpostrequestmodel.Newjobpostrequestmodel
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Data
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Prerequistquestionmodel

import technology.innovate.haziremployee.utility.DataState
import javax.inject.Inject

@HiltViewModel
class JobQuestionViewModel @Inject constructor(private val notificationsRepository: JobquestionRepository) :
    ViewModel() {
    private val _notifications = MutableLiveData<DataState<Jobquestionmodel>>()
    val notifications: LiveData<DataState<Jobquestionmodel>> = _notifications


    private val _managerjobdetails = MutableLiveData<DataState<Mangerjobdetailmanagermodel>>()
    val managerjobdetails: LiveData<DataState<Mangerjobdetailmanagermodel>> = _managerjobdetails


    private val _interviewjobdetails = MutableLiveData<DataState<Interviewjobdetailmodel>>()
    val interviewjobdetails: LiveData<DataState<Interviewjobdetailmodel>> = _interviewjobdetails

    private val _countrylist = MutableLiveData<DataState<CountrylistModel>>()
    val countrylist: LiveData<DataState<CountrylistModel>> = _countrylist


    private val _applyjob = MutableLiveData<DataState<Jobapplicationresponse>>()
    val applyjob: LiveData<DataState<Jobapplicationresponse>> = _applyjob


    private val _addnewjob= MutableLiveData<DataState<AddNewjobresponseModel>>()
    val addnewjob: LiveData<DataState<AddNewjobresponseModel>> = _addnewjob

    private val _interviewjobchangestatus= MutableLiveData<DataState<Interviewchangestatusmodel>>()
    val interviewjobchangestatus: LiveData<DataState<Interviewchangestatusmodel>> = _interviewjobchangestatus


    private val _currencylist = MutableLiveData<DataState<Currencylistmodel>>()
    val currencylist: LiveData<DataState<Currencylistmodel>> = _currencylist

    var storePickupList: List<Data> = listOf()

    var addprequestion: List<Data> = listOf()

    private val _prereqquestion = MutableLiveData<DataState<Prerequistquestionmodel>>()
    val prereqquestion: LiveData<DataState<Prerequistquestionmodel>> = _prereqquestion

//    private val _jobpostlist = MutableLiveData<DataState<JobPostlistresponse>>()
//    val jobpostlist: LiveData<DataState<JobPostlistresponse>> = _jobpostlist

    fun jobquestion(id:String) {
        viewModelScope.launch {
            notificationsRepository.jobquestion(id)
                .onEach { dataState ->
                    dataState.let {
                        _notifications.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun managerjobdetail(id:String) {
        viewModelScope.launch {
            notificationsRepository.managerjobdetail(id)
                .onEach { dataState ->
                    dataState.let {
                        _managerjobdetails.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun prereqquestion() {
        viewModelScope.launch {
            notificationsRepository.prereququestion()
                .onEach { dataState ->
                    dataState.let {
                        _prereqquestion.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }




//    fun jobpostlist(id:String) {
//        viewModelScope.launch {
//            notificationsRepository.jobpostlist(id)
//                .onEach { dataState ->
//                    dataState.let {
//                        _jobpostlist.value = it
//                    }
//                }.launchIn(viewModelScope)
//        }
//    }


    fun countrylist() {
        viewModelScope.launch {
            notificationsRepository.countrylist()
                .onEach { dataState ->
                    dataState.let {
                        _countrylist.value = it
                        Log.e("dfghjk",it.toString())
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun currencylist() {
        viewModelScope.launch {
            notificationsRepository.currencylist()
                .onEach { dataState ->
                    dataState.let {
                        _currencylist.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun applyjob(jobApplicationRequest: JobApplicationRequest) {
        viewModelScope.launch {
            notificationsRepository.applyjob(jobApplicationRequest)
                .onEach { dataState ->
                    dataState.let {
                        _applyjob.value = (it)
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun addnewjobrequest(newjobpostrequestmodel: Newjobpostrequestmodel) {
        viewModelScope.launch {
            notificationsRepository.addnewjobrequest(newjobpostrequestmodel)
                .onEach { dataState ->
                    dataState.let {
                        _addnewjob.value = (it)
                    }
                }.launchIn(viewModelScope)
        }
    }



    fun interviewroundjobdetail(id:String) {
        viewModelScope.launch {
            notificationsRepository.interviewjobdetail(id)
                .onEach { dataState ->
                    dataState.let {
                        _interviewjobdetails.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun interviewchangestatus(interviewchangestatusrequest: Interviewchangestatusrequest) {
        viewModelScope.launch {
            notificationsRepository.interviewjobchangestatus(interviewchangestatusrequest)
                .onEach { dataState ->
                    dataState.let {
                        _interviewjobchangestatus.value = (it)
                    }
                }.launchIn(viewModelScope)
        }
    }
}