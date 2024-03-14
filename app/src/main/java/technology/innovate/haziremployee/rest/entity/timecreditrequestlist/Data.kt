package technology.innovate.haziremployee.rest.entity.timecreditrequestlist


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("company_id")
    val companyId: Int, // 26
    @SerializedName("created_at")
    val createdAt: String, // 2023-06-14 11:57:51
    @SerializedName("credits_required_in_mins")
    val creditsRequiredInMins: Int, // 30
    @SerializedName("date")
    val date: String, // 2023-06-05
    @SerializedName("employee")
    val employee: Employee,
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("organisation_id")
    val organisationId: Int, // 19
    @SerializedName("reason")
    val reason: String, // Test reason
    @SerializedName("status")
    val status: String, // approved
    @SerializedName("updated_at")
    val updatedAt: String, // 2023-06-14 11:59:53
    @SerializedName("usage_type")
    val usageType: String, // early_going
    @SerializedName("user_id")
    val userId: Int // 9584
)