package technology.innovate.haziremployee.ui.requests

import technology.innovate.haziremployee.rest.entity.LeaveRequestsItem

interface LeaveClickListener {
    fun updateLeaveRequest(leaveRequestsItem: LeaveRequestsItem)
    fun onDeleteClicked(id: Int)
}