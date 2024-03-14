package technology.innovate.haziremployee.rest.entity.currencylistmodel


import com.google.gson.annotations.SerializedName

data class Currencylistmodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Currencies retrieved successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)