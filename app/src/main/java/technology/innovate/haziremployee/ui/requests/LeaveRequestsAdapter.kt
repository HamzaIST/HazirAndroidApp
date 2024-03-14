package technology.innovate.haziremployee.ui.requests

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.LeaveRequestsItem


class LeaveRequestsAdapter(val context: Context,val onClickListener: LeaveClickListener, var leaveRequests: List<LeaveRequestsItem?>) : RecyclerView.Adapter<LeaveRequestsAdapter.RequestsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
         ): RequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.leave_request_holder, parent, false)

        return RequestsViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: RequestsViewHolder,
        position: Int
    ) {
            holder.request_type.text = leaveRequests[position]?.description
            holder.metadata.text = leaveRequests[position]?.leaveType?.title
            holder.days.text = leaveRequests[position]?.days.toString() + " Days"
            holder.from_to.text = leaveRequests[position]?.fromDate + "  -  " + leaveRequests[position]?.toDate
            holder.request_on.text = leaveRequests[position]?.createdAt
            holder.status.text = leaveRequests[position]?.status
            //holder.ref_number.text = leaveRequests[position].reference_number


            if (leaveRequests[position]?.status.equals("pending")) {
                holder.status.background = ContextCompat.getDrawable(context, R.drawable.pending_bg)
                holder.status.setTextColor(context.resources.getColor(R.color.text_color_yellow))
              //  holder.delete.visibility = View.VISIBLE
                //holder.edit.visibility = View.VISIBLE
            } else if (leaveRequests[position]?.status.equals("approved")) {
                holder.status.background =
                    ContextCompat.getDrawable(context, R.drawable.approved_bg)
                holder.status.setTextColor(context.resources.getColor(R.color.text_color_green))
              //  holder.delete.visibility = View.GONE
                //holder.edit.visibility = View.GONE
            } else if (leaveRequests[position]?.status.equals("rejected")) {
                holder.status.background =
                    ContextCompat.getDrawable(context, R.drawable.declined_bg)
                holder.status.setTextColor(context.resources.getColor(R.color.text_color_red))
             //   holder.delete.visibility = View.GONE
                //holder.edit.visibility = View.GONE
            }


            holder.delete.setOnClickListener {
                onClickListener.onDeleteClicked(leaveRequests[position]?.id!!)
            }

            holder.edit.setOnClickListener {
                onClickListener.updateLeaveRequest(leaveRequests[position]!!)
            }

    }

    override fun getItemCount(): Int {
        return leaveRequests.size;
    }

    fun setData(leaveRequests: List<LeaveRequestsItem?>) {
        this.leaveRequests = leaveRequests
        notifyDataSetChanged()
    }

    class RequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request_type = itemView.findViewById<View>(R.id.request_type) as TextView
        var metadata = itemView.findViewById<View>(R.id.metadata) as TextView
        var days = itemView.findViewById<View>(R.id.days) as TextView
        var from_to = itemView.findViewById<View>(R.id.from_to) as TextView
        var request_on = itemView.findViewById<View>(R.id.request_on) as TextView
        //var ref_number = itemView.findViewById<View>(R.id.ref_number) as TextView
        var status = itemView.findViewById<View>(R.id.status) as TextView
        var delete = itemView.findViewById<View>(R.id.delete) as ImageView
        var edit = itemView.findViewById<View>(R.id.edit) as ImageView
    }
}
