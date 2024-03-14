package technology.innovate.haziremployee.rest.entity.managerjobpostrequestlist


import com.google.gson.annotations.SerializedName

data class ManagerjobpostrequestlistModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)