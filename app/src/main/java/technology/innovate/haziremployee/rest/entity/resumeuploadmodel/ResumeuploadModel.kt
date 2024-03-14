package technology.innovate.haziremployee.rest.entity.resumeuploadmodel


import com.google.gson.annotations.SerializedName

data class ResumeuploadModel(
    @SerializedName("data")
    val `data`: String, // 20230427033058_1682589658.pdf
    @SerializedName("message")
    val message: String, // Upload Successful
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 4005
)