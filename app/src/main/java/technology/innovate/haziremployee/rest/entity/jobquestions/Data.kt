package technology.innovate.haziremployee.rest.entity.jobquestions


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int, // 13
    @SerializedName("question")
    val question: String, // What is your total Exp ?
    @SerializedName("question_id")
    val questionId: Int, // 6
    @SerializedName("question_optional")
    val questionOptional: Int, // 1
    @SerializedName("question_options")
    val questionOptions: List<String>,
    @SerializedName("question_type")
    val questionType: Int // 1
)