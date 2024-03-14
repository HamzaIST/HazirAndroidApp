package technology.innovate.haziremployee.di

import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {
    fun onError(e: Throwable): String = when (e) {
        is SocketTimeoutException, is UnknownHostException -> {
            "Cannot Connect To Server, Check Internet Connection"

        }
        is ConnectException, is SocketException -> {
            "No Internet Connection.Please Try Again Later"
        }
        else -> "Some Error Occured.Please Try Again Later"
    }
}