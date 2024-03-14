package technology.innovate.haziremployee.rest.entity.newjobpostrequestmodel


import com.google.gson.annotations.SerializedName

data class Newjobpostrequestmodel(
    @SerializedName("branch_id")
    val branchId: Int, // 25
    @SerializedName("company_id")
    val companyId: Int, // 26
    @SerializedName("country_id")
    val countryId: Int, // 111
    @SerializedName("currency_id")
    val currencyId: Int, // 111
    @SerializedName("department_id")
    val departmentId: Int, // 44
    @SerializedName("designation_id")
    val designationId: Int, // 63
    @SerializedName("is_display_salary_job_page")
    val isDisplaySalaryJobPage: Int, // 0
    @SerializedName("job_category")
    val jobCategory: Int, // 3
    @SerializedName("job_description")
    val jobDescription: String, // This is a testing job description.
    @SerializedName("job_post_name")
    val jobPostName: String, // Test Jobs 3 by manager
    @SerializedName("job_responsibilities")
    val jobResponsibilities: String, // This is a testing job job_responsibilities.
    @SerializedName("job_type")
    val jobType: Int, // 2
    @SerializedName("max_experience")
    val maxExperience: String, // 3 year
    @SerializedName("max_salary")
    val maxSalary: String, // 10k
    @SerializedName("min_experience")
    val minExperience: String, // 2 year
    @SerializedName("min_salary")
    val minSalary: String, // 5k
    @SerializedName("no_of_positions")
    val noOfPositions: String, // 2
    @SerializedName("no_of_rounds")
    val noOfRounds: Int, // 2
    @SerializedName("requisites_questions")
    val requisitesQuestions: List<Int>
)