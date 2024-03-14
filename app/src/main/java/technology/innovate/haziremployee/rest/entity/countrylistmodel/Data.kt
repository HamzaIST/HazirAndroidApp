package technology.innovate.haziremployee.rest.entity.countrylistmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("currency")
    val currency: Currency,
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("title")
    val title: String // AFGHANISTAN
)