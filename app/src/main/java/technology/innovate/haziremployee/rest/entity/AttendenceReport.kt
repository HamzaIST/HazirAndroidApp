package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class AttendenceReport(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val attendanceData: AttendanceData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,


)


data class AttendanceData(
	@field:SerializedName("data")
	val data: List<DataItem?>? = null,
)


data class projectdata(
	@field:SerializedName("title")
	val title: String? = null,
)

data class DataItem(

	@field:SerializedName("date_time_unix")
	val dateTimeUnix: String? = null,

	@field:SerializedName("remarks")
	val remarks: Any? = null,

	@field:SerializedName("sources")
	val sources: String? = null,

	@field:SerializedName("mode")
	val mode: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("project")
	val leaveData: projectdata? = null

)


