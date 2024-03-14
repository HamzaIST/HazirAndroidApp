package technology.innovate.haziremployee.rest.entity.projectlistmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    val createdAt: String, // 2022-03-28 15:41:47
    @SerializedName("id")
    val id: Int, // 4
    @SerializedName("project_code")
    val projectCode: String, // Demo2
    @SerializedName("status")
    val status: String, // active
    @SerializedName("title")
    val title: String // Demo Project 2
)