package technology.innovate.haziremployee.callbacks

interface OtpVerifyCallBack {
    fun createPinCallback(otp: String?)
    fun confirmPinCallback(otp: String?)
    fun onAllDigitNotCompleted()
}

