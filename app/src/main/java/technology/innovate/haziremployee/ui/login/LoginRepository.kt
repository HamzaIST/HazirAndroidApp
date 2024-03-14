package technology.innovate.haziremployee.ui.login


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import technology.innovate.haziremployee.config.Constants.API_RESPONSE_CODE.TOKEN_EXPIRED
import technology.innovate.haziremployee.di.ErrorHandler
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
import technology.innovate.haziremployee.rest.entity.ApiResponse
import technology.innovate.haziremployee.rest.entity.LoginRequest
import technology.innovate.haziremployee.rest.entity.LoginResponse
import technology.innovate.haziremployee.rest.entity.TokenRequest
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.NetworkHelper
import javax.inject.Inject

class LoginRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {
    suspend fun login(request: LoginRequest): Flow<DataState<LoginResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.login(request)
                if (response.isSuccessful) {
                    Log.e("test",response.body().toString())
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else emit(DataState.Error(response.message()))
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
//            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun saveFcmToken(request: TokenRequest): Flow<DataState<ApiResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.notificationToken(request)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == TOKEN_EXPIRED){
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