package technology.innovate.haziremployee.rest.entity.timecreditrequestlist


import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("access_pin")
    val accessPin: String, // 9999
    @SerializedName("address")
    val address: Any, // null
    @SerializedName("attendance_exemption")
    val attendanceExemption: String, // 0
    @SerializedName("branch_id")
    val branchId: Int, // 25
    @SerializedName("category_id")
    val categoryId: Int, // 17
    @SerializedName("company_email")
    val companyEmail: String, // vandan.javiya@intelloger.com
    @SerializedName("company_id")
    val companyId: Int, // 26
    @SerializedName("cost_center")
    val costCenter: Int, // 22
    @SerializedName("created_at")
    val createdAt: String, // 2023-03-14 17:45:37
    @SerializedName("current_shift_id")
    val currentShiftId: Int, // 37
    @SerializedName("date_of_birth")
    val dateOfBirth: String, // 2023-03-14
    @SerializedName("date_of_joining")
    val dateOfJoining: String, // 2023-03-01
    @SerializedName("date_of_leaving")
    val dateOfLeaving: Any, // null
    @SerializedName("deleted_at")
    val deletedAt: Any, // null
    @SerializedName("department_id")
    val departmentId: Int, // 47
    @SerializedName("designation_id")
    val designationId: Int, // 66
    @SerializedName("email")
    val email: Any, // null
    @SerializedName("emergency_contact_number")
    val emergencyContactNumber: Any, // null
    @SerializedName("employee_id")
    val employeeId: String, // VJ111
    @SerializedName("gender")
    val gender: String, // male
    @SerializedName("geo_fencing_allowed")
    val geoFencingAllowed: Any, // null
    @SerializedName("grade_id")
    val gradeId: Int, // 40
    @SerializedName("id")
    val id: Int, // 9438
    @SerializedName("is_manager")
    val isManager: Int, // 1
    @SerializedName("is_perm_updated")
    val isPermUpdated: Int, // 0
    @SerializedName("leaving_type")
    val leavingType: Any, // null
    @SerializedName("manager_id")
    val managerId: Int, // 0
    @SerializedName("mobile_attendance_allowed")
    val mobileAttendanceAllowed: Int, // 0
    @SerializedName("mobile_login_allowed")
    val mobileLoginAllowed: String, // yes
    @SerializedName("name")
    val name: String, // Vandan Javiya
    @SerializedName("nationality_id")
    val nationalityId: Int, // 111
    @SerializedName("old_id")
    val oldId: Any, // null
    @SerializedName("organisation_id")
    val organisationId: Int, // 19
    @SerializedName("ot_allowed")
    val otAllowed: String,
    @SerializedName("personal_contact_number")
    val personalContactNumber: Any, // null
    @SerializedName("photo")
    val photo: Any, // null
    @SerializedName("qr_image")
    val qrImage: String, // qr_9438.svg
    @SerializedName("shift_id")
    val shiftId: Int, // 37
    @SerializedName("timezone_id")
    val timezoneId: Int, // 61
    @SerializedName("unique_id")
    val uniqueId: String, // VJ111
    @SerializedName("updated_at")
    val updatedAt: String, // 2023-06-09 09:39:30
    @SerializedName("user_id")
    val userId: Int, // 9584
    @SerializedName("uuid")
    val uuid: String, // 05eecec9-9478-41db-9ec7-d8bc53a46bbb
    @SerializedName("work_permit_no")
    val workPermitNo: Any // null
)