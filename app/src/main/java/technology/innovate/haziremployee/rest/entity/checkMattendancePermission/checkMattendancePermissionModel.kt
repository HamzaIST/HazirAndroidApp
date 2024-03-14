package technology.innovate.haziremployee.rest.entity.checkMattendancePermission


import com.google.gson.annotations.SerializedName

data class checkMattendancePermissionModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)