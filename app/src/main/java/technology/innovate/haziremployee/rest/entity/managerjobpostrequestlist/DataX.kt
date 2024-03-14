package technology.innovate.haziremployee.rest.entity.managerjobpostrequestlist


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("branch_name")
    val branchName: String, // India
    @SerializedName("company_name")
    val companyName: String, // DLT
    @SerializedName("department_name")
    val departmentName: String, // HR
    @SerializedName("designation_name")
    val designationName: String, // Receptionist
    @SerializedName("id")
    val id: Int, // 13
    @SerializedName("job_post_name")
    val jobPostName: String, // Test Jobs 3 by manager
    @SerializedName("job_request_status")
    val jobRequestStatus: String // Pending
)