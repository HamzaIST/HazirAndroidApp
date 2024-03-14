package technology.innovate.haziremployee.rest.entity.interviewjobdetailmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("applicant_feedback")
    val applicantFeedback: Any, // null
    @SerializedName("applicant_name")
    val applicantName: String, // Anu
    @SerializedName("applicant_status")
    val applicantStatus: Int, // 0
    @SerializedName("brief")
    val brief: String, // est
    @SerializedName("cancel_reason")
    val cancelReason: Any, // null
    @SerializedName("id")
    val id: Int, // 17
    @SerializedName("interview_date_time")
    val interviewDateTime: String, // 16-04-2023 12:35
    @SerializedName("interview_link")
    val interviewLink: Any, // null
    @SerializedName("interview_location")
    val interviewLocation: String, // Dubai - United Arab Emirates
    @SerializedName("interview_round_no")
    val interviewRoundNo: Int, // 2
    @SerializedName("interview_status")
    val interviewStatus: Int, // 2
    @SerializedName("interview_type")
    val interviewType: String, // F2F
    @SerializedName("inteviewer_name")
    val inteviewerName: String, // Gaurav Shrivastav
    @SerializedName("job_application_id")
    val jobApplicationId: Int, // 15
    @SerializedName("job_post_name")
    val jobPostName: String, // Sale Manager
    @SerializedName("latitude")
    val latitude: String, // 25.2048493
    @SerializedName("longitude")
    val longitude: String // 55.2707828
)