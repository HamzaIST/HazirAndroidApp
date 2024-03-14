package technology.innovate.haziremployee.utility

sealed class DataState<out R> {
    data class Success<T>(val item: T) : DataState<T>()
    data class Error(val error: String?) : DataState<Nothing>()
    object TokenExpired : DataState<Nothing>()
    object Loading: DataState<Nothing>()
}