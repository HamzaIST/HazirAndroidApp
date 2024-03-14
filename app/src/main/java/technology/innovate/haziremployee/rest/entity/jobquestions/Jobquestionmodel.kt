package technology.innovate.haziremployee.rest.entity.jobquestions


import com.google.gson.annotations.SerializedName

data class Jobquestionmodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)