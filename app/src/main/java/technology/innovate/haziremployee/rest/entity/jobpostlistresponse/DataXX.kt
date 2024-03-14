package technology.innovate.haziremployee.rest.entity.jobpostlistresponse


import com.google.gson.annotations.SerializedName

data class DataXX(
    @SerializedName("company_name")
    val companyName: String, // DLT
    @SerializedName("department_name")
    val departmentName: String, // HR
    @SerializedName("detail_id")
    val detailId: String, // eyJpdiI6IlJVVnp3UVNlWjN5VUR1ZDRwMVwvMGxBPT0iLCJ2YWx1ZSI6Im84c0VSelJsSTFkRFdtNTR4MFVpNFE9PSIsIm1hYyI6IjQ1MmE4YzNjOTE4NDNiZjFkYWE3NWQ5Y2E0N2IxNGMxYzBiYzc5OGI2OTBkYTY1ZjhiZGMwZTg1MDAxOTk2ZTQifQ==
    @SerializedName("job_category")
    val jobCategory: String, // WFO
    @SerializedName("job_location")
    val jobLocation: String, // INDIA
    @SerializedName("job_post_name")
    val jobPostName: String, // 2+ years Exp laravel Developer
    @SerializedName("job_type")
    val jobType: String, // full time
    @SerializedName("max_experience")
    val maxExperience: String, // 5
    @SerializedName("min_experience")
    val minExperience: String, // 2
    @SerializedName("no_of_positions")
    val noOfPositions: Int, // 3
    @SerializedName("status")
    val status: String // Open
)