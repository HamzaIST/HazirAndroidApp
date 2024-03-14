package technology.innovate.haziremployee.rest.entity.countrylistmodel


import com.google.gson.annotations.SerializedName

data class CountrylistModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Countries retrieved successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)