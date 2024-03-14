package technology.innovate.haziremployee.ui.requests

import technology.innovate.haziremployee.rest.entity.OtherRequestsItem

interface DocumentClickListener{
    fun updateDocRequest(otherRequestsItem: OtherRequestsItem)

    fun onDeleteClick(id: Int)

    fun downloadDoc(otherRequestsItem: OtherRequestsItem)
}