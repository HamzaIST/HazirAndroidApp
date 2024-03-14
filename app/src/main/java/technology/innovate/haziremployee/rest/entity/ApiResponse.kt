package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class ApiResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null


)
