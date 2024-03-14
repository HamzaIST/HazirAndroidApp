package technology.innovate.haziremployee.rest.entity.addnewjobresponse


import com.google.gson.annotations.SerializedName

data class AddNewjobresponseModel(
    @SerializedName("message")
    val message: String, // Job post saved successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)