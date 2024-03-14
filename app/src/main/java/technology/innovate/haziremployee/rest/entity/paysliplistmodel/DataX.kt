package technology.innovate.haziremployee.rest.entity.paysliplistmodel


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("amount")
    val amount: String, // 6205.00
    @SerializedName("from_date")
    val fromDate: String, // 2023-04-01
    @SerializedName("id")
    val id: Int, // 58
    @SerializedName("organisation_id")
    val organisationId: Int, // 19
    @SerializedName("payment_date")
    val paymentDate: String, // 2023-05-15
    @SerializedName("status")
    val status: String, // processed
    @SerializedName("to_date")
    val toDate: String // 2023-04-30
)