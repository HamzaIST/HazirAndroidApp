package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: UserDetails? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class UserDetails(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("organisation")
	val organisation: String? = null,

	@field:SerializedName("is_manager")
	val manager: Int? = null,


	@field:SerializedName("is_post_view")
	val ispostview: Int? = null,

	@field:SerializedName("profile_id")
	val profileid: Int? = null,

	@field:SerializedName("organisation_logo")
	val organisationLogo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("encrypt_org_id")
	val encryptorgid: String? = null,

	@field:SerializedName("organisation_id")
	val organisationid: Int? = null,


	@field:SerializedName("mobile_attendance_allowed")
	val mobileattendanceallowed: String? = null,






)
