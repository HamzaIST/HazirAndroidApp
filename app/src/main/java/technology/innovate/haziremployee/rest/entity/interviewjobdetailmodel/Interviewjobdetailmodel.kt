package technology.innovate.haziremployee.rest.entity.interviewjobdetailmodel


import com.google.gson.annotations.SerializedName

data class Interviewjobdetailmodel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // Interview round details fetched successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)