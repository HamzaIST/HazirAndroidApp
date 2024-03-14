package technology.innovate.haziremployee.rest.entity.employeecreditbalance


import com.google.gson.annotations.SerializedName

data class Employeecreditbalancemodel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // Time credit balance of employee fetched successfully.!
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)