package technology.innovate.haziremployee.rest.entity.interviewroundlistmodel


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("full_name")
    val fullName: String, // Anu
    @SerializedName("id")
    val id: Int, // 17
    @SerializedName("interview_date_time")
    val interviewDateTime: String, // 16-04-2023 12:35
    @SerializedName("interview_round_no")
    val interviewRoundNo: Int, // 2
    @SerializedName("interview_status")
    val interviewStatus: String, // Completed
    @SerializedName("interview_type")
    val interviewType: String, // F2F
    @SerializedName("job_post_name")
    val jobPostName: String, // Sale Manager
    @SerializedName("name")
    val name: String, // Gaurav Shrivastav
    @SerializedName("post_id")
    val postId: Int // 5
)