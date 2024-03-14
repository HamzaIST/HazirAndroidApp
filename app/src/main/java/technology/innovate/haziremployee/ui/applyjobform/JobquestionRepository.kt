package technology.innovate.haziremployee.ui.applyjobform

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.di.ErrorHandler
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
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
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Prerequistquestionmodel
import technology.innovate.haziremployee.rest.entity.resumeuploadmodel.ResumeuploadModel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.NetworkHelper
import javax.inject.Inject



class JobquestionRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {


    suspend fun jobquestion(id:String): Flow<DataState<Jobquestionmodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.jobpostquestion(id)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun managerjobdetail(id:String): Flow<DataState<Mangerjobdetailmanagermodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.managerjobpostdetail(id)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun prereququestion(): Flow<DataState<Prerequistquestionmodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.prereqquestion()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }



    suspend fun uploadresume(file_name: MultipartBody.Part?): Flow<DataState<ResumeuploadModel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.uploadResume(file_name)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                    Log.e("userResponse",response.message())

                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }



    suspend fun countrylist(): Flow<DataState<CountrylistModel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.countrylist()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun currencylist(): Flow<DataState<Currencylistmodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.currencylist()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun applyjob(jobApplicationRequest: JobApplicationRequest): Flow<DataState<Jobapplicationresponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.applyjob(jobApplicationRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun addnewjobrequest(newjobpostrequestmodel: Newjobpostrequestmodel): Flow<DataState<AddNewjobresponseModel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.addnewjob(newjobpostrequestmodel)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))

                        Log.e("error", response.message())
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun interviewjobdetail(id:String): Flow<DataState<Interviewjobdetailmodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.interviewjobpostdetail(id)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun interviewjobchangestatus(interviewchangestatusrequest: Interviewchangestatusrequest): Flow<DataState<Interviewchangestatusmodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.changeapplicationstatus(interviewchangestatusrequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))

                        Log.e("error", response.message())
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


}