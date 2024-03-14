package technology.innovate.haziremployee.rest.entity.designationlist


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int, // 63
    @SerializedName("title")
    val title: String // Receptionist
)