package technology.innovate.haziremployee.rest.entity

class LoginRequest(
    var device_token : String,
    var password : String,
    var username : String,
   var organisation_name : String,
)