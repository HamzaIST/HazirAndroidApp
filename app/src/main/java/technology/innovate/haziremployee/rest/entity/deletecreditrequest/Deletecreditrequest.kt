package technology.innovate.haziremployee.rest.entity.deletecreditrequest


import com.google.gson.annotations.SerializedName

data class Deletecreditrequest(
    @SerializedName("message")
    val message: String, // Time credit request removed successfully.!
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)