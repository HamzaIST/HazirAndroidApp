package technology.innovate.haziremployee.rest.entity.creditlistdetail


import com.google.gson.annotations.SerializedName

data class CreditlistdetailModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // Time credit request fetched successfully.!
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)