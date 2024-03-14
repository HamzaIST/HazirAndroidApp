package technology.innovate.haziremployee.rest.entity

class CheckOutRequest(
    var date : String,
    var time : String,
    var settings_project_id : String,
    var lat_long : String,
    var out_mode : Int,
)