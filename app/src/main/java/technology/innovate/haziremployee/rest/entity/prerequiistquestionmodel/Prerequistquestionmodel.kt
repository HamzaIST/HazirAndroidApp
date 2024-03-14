package technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel


import com.google.gson.annotations.SerializedName

data class Prerequistquestionmodel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)