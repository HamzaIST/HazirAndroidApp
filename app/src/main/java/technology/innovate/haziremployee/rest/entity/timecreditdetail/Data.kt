package technology.innovate.haziremployee.rest.entity.timecreditdetail


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("company_id")
    val companyId: Int, // 26
    @SerializedName("created_at")
    val createdAt: String, // 2023-06-14 16:03:29
    @SerializedName("credits_required_in_mins")
    val creditsRequiredInMins: Int, // 30
    @SerializedName("date")
    val date: String, // 2023-06-05
    @SerializedName("employee")
    val employee: Employee,
    @SerializedName("id")
    val id: Int, // 7
    @SerializedName("organisation_id")
    val organisationId: Int, // 19
    @SerializedName("reason")
    val reason: String, // Test reason
    @SerializedName("status")
    val status: String, // pending
    @SerializedName("updated_at")
    val updatedAt: String, // 2023-06-14 16:03:29
    @SerializedName("usage_type")
    val usageType: String, // early_going
    @SerializedName("user_id")
    val userId: Int // 10316
)