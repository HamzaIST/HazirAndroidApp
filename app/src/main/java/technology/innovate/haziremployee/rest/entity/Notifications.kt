package technology.innovate.haziremployee.rest.entity

import com.google.gson.annotations.SerializedName

data class Notifications(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val notificationData: List<NotificationData?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class NotificationData(

	@field:SerializedName("date_of_notification")
	val dateOfNotification: String? = null,

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("date_of_notification_with_time")
	val dateOfNotificationWithTime: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
