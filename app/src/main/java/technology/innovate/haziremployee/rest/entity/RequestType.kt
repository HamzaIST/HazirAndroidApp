package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class RequestType(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val requestTypeItem: List<RequestTypeItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RequestTypeItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null
)
