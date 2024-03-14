package technology.innovate.haziremployee.rest.entity.paysliplistmodel


import com.google.gson.annotations.SerializedName

data class Paysliplistmodel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // Payrolls retrived SuccessFully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: String // 200
)