package technology.innovate.haziremployee.rest.entity.jobapplicationresponse


import com.google.gson.annotations.SerializedName

data class Jobapplicationresponse(
    @SerializedName("message")
    val message: String, // Job application insert successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)