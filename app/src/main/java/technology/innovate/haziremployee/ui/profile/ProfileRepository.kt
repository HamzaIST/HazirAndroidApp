package technology.innovate.haziremployee.ui.profile


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.di.ErrorHandler
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.rest.entity.employeecreditbalance.Employeecreditbalancemodel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.NetworkHelper
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {
    suspend fun getProfile(): Flow<DataState<Profile>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.getProfile()
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

    suspend fun getEmployeecreditbalance(): Flow<DataState<Employeecreditbalancemodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.getEmployeecreditbalance()
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
    suspend fun getLeaves(): Flow<DataState<GetLeave>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.leaves()
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


    suspend fun updateProfile(request: UpdateProfile): Flow<DataState<Profile>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.updateProfile(request)
                if (response.isSuccessful) {
                    val response = response.body()
                    emit(DataState.Success(response!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                        emit(DataState.TokenExpired)
                    } else {
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

}