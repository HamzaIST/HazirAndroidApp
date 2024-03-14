package technology.innovate.haziremployee.ui.check_in


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.di.ErrorHandler
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.NetworkHelper
import javax.inject.Inject

class ActionRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {
    suspend fun checkIn(checkInRequest: CheckInRequest): Flow<DataState<CheckInResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.checkIN(checkInRequest)
//                println("Checkin request = ${Gson().toJson(checkInRequest)}")
//                println("response checkin= ${Gson().toJson(response)}")

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
           // Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun breakIn(breakInRequest: BreakInRequest): Flow<DataState<BreakInResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.breakIN(breakInRequest)

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

    suspend fun breakOut(breakOutRequest: BreakOutRequest): Flow<DataState<BreakOutResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.breakOut(breakOutRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else{
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

    suspend fun checkOut(checkOutRequest: CheckOutRequest): Flow<DataState<CheckOutResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.checkOut(checkOutRequest)
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