package technology.innovate.haziremployee.rest.entity.currencylistmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("currency")
    val currency: String, // AFN
    @SerializedName("id")
    val id: Int // 1
)