package technology.innovate.haziremployee.rest.entity.managerjobdetailmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("branch name")
    val branchName: String, // India
    @SerializedName("company name")
    val companyName: String, // DLT
    @SerializedName("country")
    val country: String, // INDIA
    @SerializedName("created_at")
    val createdAt: String, // 2023-05-10 18:14:08
    @SerializedName("currency")
    val currency: Any, // null
    @SerializedName("department")
    val department: String, // HR
    @SerializedName("designation")
    val designation: String, // Receptionist
    @SerializedName("is_display_salary_job_page")
    val isDisplaySalaryJobPage: Int, // 0
    @SerializedName("job_category")
    val jobCategory: String, // Hybrid
    @SerializedName("job_description")
    val jobDescription: String, // This is a testing job description.
    @SerializedName("job_post_name")
    val jobPostName: String, // Test Jobs 3 by manager
    @SerializedName("job_request_by")
    val jobRequestBy: Any, // null
    @SerializedName("job_request_reason")
    val jobRequestReason: Any, // null
    @SerializedName("job_request_status")
    val jobRequestStatus: String, // Pending
    @SerializedName("job_responsibilities")
    val jobResponsibilities: String, // This is a testing job job_responsibilities.
    @SerializedName("job_type")
    val jobType: String, // Part Time
    @SerializedName("max_experience")
    val maxExperience: String, // 3 year
    @SerializedName("max_salary")
    val maxSalary: String, // 10k
    @SerializedName("min_experience")
    val minExperience: String, // 2 year
    @SerializedName("min_salary")
    val minSalary: String, // 5k
    @SerializedName("no_of_positions")
    val noOfPositions: Int, // 2
    @SerializedName("no_of_rounds")
    val noOfRounds: Int, // 2
    @SerializedName("pre_requisites_questions")
    val preRequisitesQuestions: List<PreRequisitesQuestion>,
    @SerializedName("status")
    val status: String, // Open
    @SerializedName("updated_at")
    val updatedAt: String // 2023-05-10 18:14:08
)