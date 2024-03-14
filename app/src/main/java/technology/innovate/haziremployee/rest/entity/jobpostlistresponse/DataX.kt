package technology.innovate.haziremployee.rest.entity.jobpostlistresponse


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("current_page")
    val currentPage: Int, // 1
    @SerializedName("data")
    val `data`: List<DataXX>,
    @SerializedName("first_page_url")
    val firstPageUrl: String, // https://staging.dubaileading.technology/maccess-saas/api/public/api/admin/jobPostLists?page=1
    @SerializedName("from")
    val from: Int, // 1
    @SerializedName("last_page")
    val lastPage: Int, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String, // https://staging.dubaileading.technology/maccess-saas/api/public/api/admin/jobPostLists?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any, // null
    @SerializedName("path")
    val path: String, // https://staging.dubaileading.technology/maccess-saas/api/public/api/admin/jobPostLists
    @SerializedName("per_page")
    val perPage: String, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any, // null
    @SerializedName("to")
    val to: Int, // 6
    @SerializedName("total")
    val total: Int // 6
)