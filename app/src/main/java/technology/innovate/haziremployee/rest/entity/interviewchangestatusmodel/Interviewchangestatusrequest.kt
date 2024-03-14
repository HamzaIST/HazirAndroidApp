package technology.innovate.haziremployee.rest.entity.interviewchangestatusmodel


import com.google.gson.annotations.SerializedName

data class Interviewchangestatusrequest(
    @SerializedName("applicant_feedback")
    val applicantFeedback: String, // This is a testing applicant feedback.
    @SerializedName("applicant_status")
    val applicantStatus: Int?, // 2
    @SerializedName("interview_round_id")
    val interviewRoundId: Int // 17
)