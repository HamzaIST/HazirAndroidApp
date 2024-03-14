package technology.innovate.haziremployee.rest.entity.jobcompanymodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int, // 26
    @SerializedName("title")
    val title: String // DLT
)