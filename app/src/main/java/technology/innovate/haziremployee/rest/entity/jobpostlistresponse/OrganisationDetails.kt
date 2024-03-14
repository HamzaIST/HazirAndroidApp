package technology.innovate.haziremployee.rest.entity.jobpostlistresponse


import com.google.gson.annotations.SerializedName

data class OrganisationDetails(
    @SerializedName("id")
    val id: Int, // 19
    @SerializedName("logo")
    val logo: Any, // null
    @SerializedName("title")
    val title: String // Demo Company
)