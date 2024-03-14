package technology.innovate.haziremployee.rest.entity.departmentlistmodel


import com.google.gson.annotations.SerializedName

data class Depalrtmentlistresponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Departments retrieved successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)