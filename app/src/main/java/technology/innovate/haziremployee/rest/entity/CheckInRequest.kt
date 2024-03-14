package technology.innovate.haziremployee.rest.entity

class CheckInRequest (
    var mode : Int,
    var date : String,
    var time : String,
    var lat_long : String,
    var app_version : String,
    var settings_project_id : Int,
)