package technology.innovate.haziremployee.rest.entity.applycreditrequest


import com.google.gson.annotations.SerializedName

data class Applycreditresponsemodel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // Time credit request added successfully.
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)