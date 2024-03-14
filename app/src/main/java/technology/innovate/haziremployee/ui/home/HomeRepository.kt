package technology.innovate.haziremployee.ui.home


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.di.ErrorHandler
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.rest.entity.checkMattendancePermission.checkMattendancePermissionModel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.NetworkHelper
import javax.inject.Inject

class HomeRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {
    suspend fun posts(): Flow<DataState<Posts>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.posts()
                println("response code() = ${response.code()}")

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

    suspend fun likePost(request: LikePost): Flow<DataState<ApiResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.likePost(request)
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

    suspend fun notificationCount(): Flow<DataState<NotificationCount>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.notificationCount()
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


    suspend fun checkMattendancePermission(): Flow<DataState<checkMattendancePermissionModel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.checkMattendancePermission()
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

}