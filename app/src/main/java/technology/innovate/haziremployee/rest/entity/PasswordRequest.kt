package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class PasswordRequest(

	@field:SerializedName("current_password")
	val currentPassword: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: String? = null
)
