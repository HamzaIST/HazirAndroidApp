package technology.innovate.haziremployee.rest.entity.timecreditrequestlist


import com.google.gson.annotations.SerializedName

data class TimecreaditrequestlistModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String, // Employee time credit Requests fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)