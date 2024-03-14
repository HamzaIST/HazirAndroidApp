package technology.innovate.haziremployee.rest.entity.managerjobdetailmodel


import com.google.gson.annotations.SerializedName

data class PreRequisitesQuestion(
    @SerializedName("id")
    val id: Int, // 32
    @SerializedName("question")
    val question: String, // Question 1
    @SerializedName("question_id")
    val questionId: Int // 1
)