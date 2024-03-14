package technology.innovate.haziremployee.rest.entity.managerjobdetailmodel


import com.google.gson.annotations.SerializedName

data class Mangerjobdetailmanagermodel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // Job post request details fetched successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)