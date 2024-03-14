package technology.innovate.haziremployee.rest.entity.interviewchangestatusmodel


import com.google.gson.annotations.SerializedName

data class Interviewchangestatusmodel(
    @SerializedName("message")
    val message: String, // Applicant status updated successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)