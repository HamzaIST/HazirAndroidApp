package technology.innovate.haziremployee.rest.entity.applycreditrequest


import com.google.gson.annotations.SerializedName

data class Newcreditrequestmodel(
    @SerializedName("credits_required_in_mins")
    val creditsRequiredInMins: Int, // 30
    @SerializedName("date")
    val date: String, // 05-06-2023
    @SerializedName("reason")
    val reason: String, // Test reason
    @SerializedName("usage_type")
    val usageType: String // early_going
)