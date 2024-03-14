package technology.innovate.haziremployee.rest.endpoints

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.rest.entity.addnewjobresponse.AddNewjobresponseModel
import technology.innovate.haziremployee.rest.entity.applycreditrequest.Applycreditresponsemodel
import technology.innovate.haziremployee.rest.entity.applycreditrequest.Newcreditrequestmodel
import technology.innovate.haziremployee.rest.entity.branchlistmodel.Branchlistmodel
import technology.innovate.haziremployee.rest.entity.checkMattendancePermission.checkMattendancePermissionModel
import technology.innovate.haziremployee.rest.entity.countrylistmodel.CountrylistModel
import technology.innovate.haziremployee.rest.entity.creditlistdetail.CreditlistdetailModel
import technology.innovate.haziremployee.rest.entity.currencylistmodel.Currencylistmodel
import technology.innovate.haziremployee.rest.entity.deletecreditrequest.Deletecreditrequest
import technology.innovate.haziremployee.rest.entity.departmentlistmodel.Depalrtmentlistresponse
import technology.innovate.haziremployee.rest.entity.designationlist.Designationlistmodel
import technology.innovate.haziremployee.rest.entity.employeecreditbalance.Employeecreditbalancemodel
import technology.innovate.haziremployee.rest.entity.interviewchangestatusmodel.Interviewchangestatusmodel
import technology.innovate.haziremployee.rest.entity.interviewchangestatusmodel.Interviewchangestatusrequest
import technology.innovate.haziremployee.rest.entity.interviewjobdetailmodel.Interviewjobdetailmodel
import technology.innovate.haziremployee.rest.entity.interviewroundlistmodel.Interviewroundlistmodel
import technology.innovate.haziremployee.rest.entity.jobapplicationrequest.JobApplicationRequest
import technology.innovate.haziremployee.rest.entity.jobapplicationresponse.Jobapplicationresponse
import technology.innovate.haziremployee.rest.entity.jobcompanymodel.Jobcomapanymodel
import technology.innovate.haziremployee.rest.entity.jobpostlistresponse.Jobpostlistresponse
import technology.innovate.haziremployee.rest.entity.jobquestions.Jobquestionmodel
import technology.innovate.haziremployee.rest.entity.managerjobdetailmodel.Mangerjobdetailmanagermodel
import technology.innovate.haziremployee.rest.entity.managerjobpostrequestlist.ManagerjobpostrequestlistModel
import technology.innovate.haziremployee.rest.entity.newjobpostrequestmodel.Newjobpostrequestmodel
import technology.innovate.haziremployee.rest.entity.paysliplistmodel.Paysliplistmodel
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Prerequistquestionmodel
import technology.innovate.haziremployee.rest.entity.projectlistmodel.Projectlistmodel
import technology.innovate.haziremployee.rest.entity.resumeuploadmodel.ResumeuploadModel
import technology.innovate.haziremployee.rest.entity.timecreditrequestlist.TimecreaditrequestlistModel


interface EmployeeEndpoint {
    @POST("employee/login")
    suspend fun login(@Body loginRequest: LoginRequest?): Response<LoginResponse?>

    @POST("employee/checkIN")
    suspend fun checkIN(@Body checkInRequest: CheckInRequest?): Response<CheckInResponse?>

    @POST("employee/breakIN")
    suspend fun breakIN(@Body breakInRequest: BreakInRequest?): Response<BreakInResponse?>

    @POST("employee/breakOUT")
    suspend fun breakOut(@Body breakOutRequest: BreakOutRequest?): Response<BreakOutResponse?>

    @POST("employee/checkOUT")
    suspend fun checkOut(@Body breakOutRequest: CheckOutRequest?): Response<CheckOutResponse?>

    @POST("employee/EmployeeAttendenceReport")
    suspend fun getAttendanceReport(@Body reportRequest: ReportRequest?): Response<AttendenceReport?>

    @POST("employee/notificationList")
    suspend fun notifications(): Response<Notifications?>

    @GET("employee/listPostsnew")
    suspend fun posts(): Response<Posts?>

    @POST("employee/likePostnew")
    suspend fun likePost(@Body likePost: LikePost?): Response<ApiResponse?>

    @GET("employee/employeeProfileView")
    suspend fun getProfile(): Response<Profile?>

    @GET("employeeCreditBalance")
    suspend fun getEmployeecreditbalance(): Response<Employeecreditbalancemodel>

    @POST("employee/EmployeeUpdateProfile")
    suspend fun updateProfile(@Body updateProfile: UpdateProfile?): Response<Profile?>

    @GET("employee/getLeaveDetails")
    suspend fun leaves(): Response<GetLeave>

    @POST("employee/changeEmployeePassword")
    suspend fun changePassword(@Body passwordRequest: PasswordRequest?): Response<ChangePassword?>

    @GET("getLeaveTypesnew")
    suspend fun leaveTypes(): Response<LeaveTypes?>

    @GET("getRequestTypes")
    suspend fun requestTypes(): Response<RequestType?>

    @Multipart
    @POST("applyLeaveApplicationnew")
    suspend fun applyLeaveWithFile(
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Response<ApiResponse2?>

    @Multipart
    @POST("applyLeaveApplicationnew")
    suspend fun applyLeaveWithoutFile(
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Response<ApiResponse2?>

    @Multipart
    @POST("updateEmployeeLeaveRequest")
    suspend fun updateLeaveWithFile(
        @Part("id") id: RequestBody?,
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Response<ApiResponse2?>

    @Multipart
    @POST("updateEmployeeLeaveRequest")
    suspend fun updateLeaveWithoutFile(
        @Part("id") id: RequestBody?,
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Response<ApiResponse2?>

    @Multipart
    @POST("makeEmployeeRequestnew")
    suspend fun requestDocumentWithFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part,
    ): Response<ApiResponse?>

    @Multipart
    @POST("makeEmployeeRequestnew")
    suspend fun requestDocumentWithoutFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("email") email: RequestBody?
    ): Response<ApiResponse?>

    @Multipart
    @POST("updateEmployeeDocRequest")
    suspend fun updateEmployeeDocRequestWithFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("id") id: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Response<ApiResponse?>

    @Multipart
    @POST("updateEmployeeDocRequest")
    suspend fun updateEmployeeDocRequestWithoutFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("id") id: RequestBody?
    ): Response<ApiResponse?>

    @POST("getEmployeeRequestsnew")
    suspend fun getEmployeeRequests(@Body getRequests: GetRequests?): Response<EmployeeRequests?>

    @POST("employee-forgot-password")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPassword?): Response<ApiResponse?>

    @POST("employee-resend-otp")
    suspend fun resendOTP(@Body forgotPassword: ForgotPassword?): Response<ApiResponse?>

    @POST("employee-verify-otp")
    suspend fun verifyOTP(@Body verifyOTP: VerifyOTP?): Response<ApiResponse?>

    @POST("employee-reset-password")
    suspend fun resetPassword(@Body resetPassword: ResetPassword?): Response<ApiResponse?>

    @POST("employee/notificationToken")
    suspend fun notificationToken(@Body tokenRequest: TokenRequest?): Response<ApiResponse?>

    @POST("employeeDeleteDocRequest")
    suspend fun deleteDocRequest(@Body deleteReq: DeleteReq?): Response<ApiResponse?>

    @POST("employeeDeleteLeaveRequest")
    suspend fun deleteLeaveRequest(@Body deleteReq: DeleteReq?): Response<ApiResponse?>

    @GET("employee/unreadnotificationListCount")
    suspend fun notificationCount(): Response<NotificationCount?>


    @POST("employee/checkMattendancePermission")
    suspend fun checkMattendancePermission(): Response<checkMattendancePermissionModel?>
///
    @GET("admin/getJobPreRequiQuestions/{id}")
    suspend fun jobpostquestion(
        @Path("id") id: String,
    ): Response<Jobquestionmodel?>


    @GET("manager/detailsJobPostRequest/{id}")
    suspend fun managerjobpostdetail(
        @Path("id") id: String,
    ): Response<Mangerjobdetailmanagermodel?>


    @Multipart
    @POST("uploadResume")
    suspend fun uploadResume(
        @Part file_name: MultipartBody.Part?
    ): Response<ResumeuploadModel?>

    @GET("admin/getCountries")
    suspend fun countrylist(
    ): Response<CountrylistModel?>


    @GET("admin/getCurrencies")
    suspend fun currencylist(
    ): Response<Currencylistmodel?>

    @POST("employeeJobApply")
    suspend fun applyjob(@Body applyjob: JobApplicationRequest?): Response<Jobapplicationresponse?>


    @Multipart
    @POST("admin/jobPostLists")
    suspend fun jobpostlist(
        @Part("organisation_id") perpage: String?,
        @Part("page") page: Int?,
        @Part("department_id") departmentid: Int?,
        @Part("designation_id") designationid: Int?,
        @Part("job_category") jobcategory: Int?,
        @Part("status") status: Int?,
        @Part("items_per_page") items_per_page: Int?,

        ): Response<Jobpostlistresponse?>

    @GET("admin/departmentLists/{id}")
    suspend fun departmentlist(
        @Path("id") id: String,
    ): Response<Depalrtmentlistresponse?>


    @GET("admin/designationLists/{id}")
    suspend fun designationlist(
        @Path("id") id: String,
    ): Response<Designationlistmodel?>


    @Multipart
    @POST("manager/jobPostRequestList")
    suspend fun managerjobpostlist(
        @Part("page") page: Int?,
        @Part("items_per_page") itemperpage: Int?,
        @Part("department_id") departmentid: Int?,
        @Part("designation_id") designationid: Int?,
        @Part("job_category") jobcategory: Int?,
        @Part("job_request_status") status: Int?,

        ): Response<ManagerjobpostrequestlistModel?>


    @GET("manager/getBranches")
    suspend fun getbranchlist(
    ): Response<Branchlistmodel?>
    @GET("manager/getCompanies")
    suspend fun getcompanylist(
    ): Response<Jobcomapanymodel?>


    @GET("manager/getAllPreRequisitesQuestions")
    suspend fun prereqquestion(
    ): Response<Prerequistquestionmodel?>

    @POST("manager/addJobPostRequest")
    suspend fun addnewjob(@Body addNewJobPost: Newjobpostrequestmodel): Response<AddNewjobresponseModel?>

    @Multipart
    @POST("manager/interviewRoundsList")
    suspend fun interviewroundlist(
        @Part("page") page: Int?,
        @Part("items_per_page") itemperpage: Int?,
        @Part("job_application_id") jobapplicationid: Int?,
        @Part("interview_status") interviewstatus: Int?,
        @Part("interview_date") interviewdate: String?,
        @Part("interview_type") interviewtype: Int?,

        ): Response<Interviewroundlistmodel?>

    @GET("admin/viewInterviewRound/{id}")
    suspend fun interviewjobpostdetail(
        @Path("id") id: String,
    ): Response<Interviewjobdetailmodel?>

    @POST("manager/changeApplicantStatus")
    suspend fun changeapplicationstatus(@Body interviewchangestatusrequest: Interviewchangestatusrequest): Response<Interviewchangestatusmodel?>

    @POST("employee/payrollsList")
    suspend fun paysliplist(@Body reportRequest: ReportRequest?): Response<Paysliplistmodel?>

    @POST("employee/projectLists")
    suspend fun projectlist(

    ): Response<Projectlistmodel?>

    @POST("listTimeCreditRequestsEmployee")
    suspend fun listtimecredit(): Response<TimecreaditrequestlistModel?>

    @POST("deleteTimeCreditRequest/{id}")
    suspend fun deleteTimeCreditRequest(
        @Path("id") id: String,
    ): Response<Deletecreditrequest?>
    @GET("viewTimeCreditRequest/{id}")
    suspend fun viewTimeCreditRequest(
        @Path("id") id: String,
    ): Response<CreditlistdetailModel?>


    @POST("requestTimeCredit")
    suspend fun applycreditrequest(@Body newcreditrequestmodel: Newcreditrequestmodel?): Response<Applycreditresponsemodel?>
}
