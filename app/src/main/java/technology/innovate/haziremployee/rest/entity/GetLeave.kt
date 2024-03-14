package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class GetLeave(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: LeaveData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LeaveData(

	@field:SerializedName("sum_available_leave")
	val sumAvailableLeave: Int? = null,

	@field:SerializedName("leave_requested")
	val leaveRequested: Int? = null,

	@field:SerializedName("sum_total_leave")
	val sumTotalLeave: Int? = null,

	@field:SerializedName("leave_data")
	val leaveData: List<LeaveDataItem?>? = null
)

data class LeaveDataItem(

	@field:SerializedName("balance_leaves")
	val remainingLeaves: String? = "0",

	@field:SerializedName("opening_leave_balance")
	val totalLeaves: String? = "0",

	@field:SerializedName("leave_title")
	val leavetitle: String? = null,

	@field:SerializedName("leave_code")
	val leaveCode: String? = null
)
