package technology.innovate.haziremployee.rest.entity.projectlistmodel


import com.google.gson.annotations.SerializedName

data class Projectlistmodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)