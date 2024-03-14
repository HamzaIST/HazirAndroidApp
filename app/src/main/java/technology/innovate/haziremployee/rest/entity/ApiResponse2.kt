package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class ApiResponse2(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
