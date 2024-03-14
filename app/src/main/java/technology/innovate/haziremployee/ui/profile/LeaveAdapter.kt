package technology.innovate.haziremployee.ui.profile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.LeaveDataItem


class LeaveAdapter(var context: Context) : RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder>() {
    private var dataList = ArrayList<LeaveDataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_leave_type, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
       // holder.leaveLeft_Heading.text = dataList[position].leaveCode+" Left"
        holder.leaveLeft_Heading.text = dataList[position].leavetitle+" Balance"
        Log.e("remaninigleaves",dataList[position].remainingLeaves.toString())
        holder.leaveLeft.text = dataList[position].remainingLeaves.toString()
        holder.leaveTotal.text = " out of "+dataList[position].totalLeaves.toString()

        if (position % 4 == 0){
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.sl_circular_dot_bg))
        } else if (position % 4 == 1){
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cl_circular_dot_bg))
        } else if (position % 4 == 2){
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.al_circular_dot_bg))
        }else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.sl_circular_dot_bg))
        }

    }

    override fun getItemCount(): Int {
        return dataList.size;
    }

    fun addList(leaveDataItemList : ArrayList<LeaveDataItem>){
        dataList.clear()
        dataList.addAll(leaveDataItemList)
        notifyDataSetChanged()
    }

    class LeaveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leaveLeft_Heading = itemView.findViewById<View>(R.id.leaveLeft_Heading) as TextView
        var leaveLeft = itemView.findViewById<View>(R.id.leaveLeft) as TextView
        var leaveTotal = itemView.findViewById<View>(R.id.leaveTotal) as TextView
        var imageView = itemView.findViewById<View>(R.id.imageView) as ImageView

    }
}
