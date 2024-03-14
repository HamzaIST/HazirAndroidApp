package technology.innovate.haziremployee.rest.entity.designationlist


import com.google.gson.annotations.SerializedName

data class Designationlistmodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Designation retrieved successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)