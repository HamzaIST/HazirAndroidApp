package technology.innovate.haziremployee.rest.entity.jobapplicationrequest


import com.google.gson.annotations.SerializedName

data class JobApplicationRequest(
    @SerializedName("brief_profile_summary")
    val briefProfileSummary: String,
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("current_company_name")
    val currentCompanyName: String,
    @SerializedName("education")
    val education: String,
    @SerializedName("email")
    val email: String, // abc@gmail.com
    @SerializedName("external_source")
    val externalSource: String,
    @SerializedName("full_name")
    val fullName: String, // Test D Test
    @SerializedName("job_post_id")
    val jobPostId: String, // eyJpdiI6Im1Kb3dIelJadmVhSlwvY1RFVzFsZFdRPT0iLCJ2YWx1ZSI6InVleWQyVHNkV3U1SUpCXC9BNWRyMWRBPT0iLCJtYWMiOiIzNWFmOTJkNzAyNWFiNDQ2YTJkNjcyNGRlOWQ2ZTU5MmE2YjZjYzdjMWFlYzcxNjRhNzNmMWVjYjI2YTRjNWFkIn0=
    @SerializedName("job_question_answer")
    val jobQuestionAnswer: List<Any>,
    @SerializedName("notice_period_days")
    val noticePeriodDays: String,
    @SerializedName("phone_number")
    val phoneNumber: String, // 1234567890
    @SerializedName("relavant_experience_month")
    val relavantExperienceMonth: String,
    @SerializedName("resume_file_name")
    val resumeFileName: String, // 20230315031100_1678873260.pdf
    @SerializedName("salary_expectation")
    val salaryExpectation: String,
    @SerializedName("salary_expectation_currency")
    val salaryExpectationCurrency: String,
    @SerializedName("total_experience_month")
    val totalExperienceMonth: String,
    @SerializedName("university")
    val university: String
)