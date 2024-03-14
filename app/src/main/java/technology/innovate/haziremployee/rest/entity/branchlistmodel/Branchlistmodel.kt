package technology.innovate.haziremployee.rest.entity.branchlistmodel


import com.google.gson.annotations.SerializedName

data class Branchlistmodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Branches retrieved successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)