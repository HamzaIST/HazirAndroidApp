package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class LeaveTypes(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: List<LeaveTypeData?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class  LeaveTypeData(

	@field:SerializedName("balance_leaves")
	val balanceLeaves: String? = null,

	@field:SerializedName("opening_leave_balance")
	val noOfLeaves: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("short_code")
	val shortCode: String? = null,

	@field:SerializedName("leave_cycle_from")
	val leave_cycle_from: String? = null,

	@field:SerializedName("leave_cycle_to")
	val leave_cycle_to: String? = null,


)
