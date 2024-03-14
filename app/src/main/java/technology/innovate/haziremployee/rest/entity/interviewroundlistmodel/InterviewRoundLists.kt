package technology.innovate.haziremployee.rest.entity.interviewroundlistmodel


import com.google.gson.annotations.SerializedName

data class InterviewRoundLists(
    @SerializedName("current_page")
    val currentPage: Int, // 1
    @SerializedName("data")
    val `data`: List<DataX>,
    @SerializedName("first_page_url")
    val firstPageUrl: String, // https://staging.dubaileading.technology/maccess-saas/api/public/api/manager/interviewRoundsList?page=1
    @SerializedName("from")
    val from: Int, // 1
    @SerializedName("last_page")
    val lastPage: Int, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String, // https://staging.dubaileading.technology/maccess-saas/api/public/api/manager/interviewRoundsList?page=1
    @SerializedName("next_page_url")
    val nextPageUrl: Any, // null
    @SerializedName("path")
    val path: String, // https://staging.dubaileading.technology/maccess-saas/api/public/api/manager/interviewRoundsList
    @SerializedName("per_page")
    val perPage: String, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any, // null
    @SerializedName("to")
    val to: Int, // 1
    @SerializedName("total")
    val total: Int // 1
)