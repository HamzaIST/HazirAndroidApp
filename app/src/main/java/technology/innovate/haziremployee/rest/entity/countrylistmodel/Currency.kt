package technology.innovate.haziremployee.rest.entity.countrylistmodel


import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("country_id")
    val countryId: Int, // 1
    @SerializedName("created_at")
    val createdAt: String, // 2020-12-22 09:22:53
    @SerializedName("deleted_at")
    val deletedAt: Any, // null
    @SerializedName("id")
    val id: Int, // 240
    @SerializedName("status")
    val status: String, // active
    @SerializedName("title")
    val title: String, // AWG
    @SerializedName("updated_at")
    val updatedAt: String // 2020-12-22 09:22:53
)