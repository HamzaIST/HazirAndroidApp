package technology.innovate.haziremployee.rest.entity.jobcompanymodel


import com.google.gson.annotations.SerializedName

data class Jobcomapanymodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Companies retrieved successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)