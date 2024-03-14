package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class Profile(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val profileData: ProfileData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)



data class ProfileData(

	@field:SerializedName("designation")
	val designation: Designation? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("personal_contact_number")
	val personalContactNumber: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

)

data class Designation(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
