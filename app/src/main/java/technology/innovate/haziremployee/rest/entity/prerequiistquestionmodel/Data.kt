package technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int, // 6
    @SerializedName("question")
    val question: String // What is your total Exp ?
)