package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class Posts(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<PostData?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PostData(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("readable_created_at")
	val readableCreatedAt: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("liked_users_count")
	var likedUsersCount: Int? = null,

	@field:SerializedName("liked")
	var liked: Boolean? = null,
)
