package technology.innovate.haziremployee.rest.entity.checkMattendancePermission


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("is_post_view")
    val isPostView: Int, // 0
    @SerializedName("request_module_access")
    val requestmoduleaccess: Int, // 0
    @SerializedName("mobile_attendance_allowed")
    val mobileAttendanceAllowed: Int,
    @SerializedName("user_status")
    val userstatus: Int, /// 1
    @SerializedName("mobile_login_allowed")
    val mobileloginallowed: Int /// 1
)